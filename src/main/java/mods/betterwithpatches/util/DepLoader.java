package mods.betterwithpatches.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.common.versioning.ComparableVersion;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.LaunchClassLoader;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * For autodownloading Better With Mods and nothing else.
 * Basically a copy of @link codechicken.core.launch.DepLoader, slightly mangled.
 * Loads a file separate from "dependæncies.info" to let the actual DepLoader have its fun.
 */
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class DepLoader implements IFMLLoadingPlugin, IFMLCallHook, IEarlyMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.betterwithpatches.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return new BWPMixinLoader(true).getMixins(loadedCoreMods);
    }

    private static final ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(1 << 23);
    private static final String owner = new Random().nextInt(231) == 0 ? "Sneed's DepLoader" : "CB's DepLoader";
    private static DepLoader.DepLoadInst inst;

    public interface IDownloadDisplay {
        void resetProgress(int sizeGuess);

        void setPokeThread(Thread currentThread);

        void updateProgress(int fullLength);

        boolean shouldStopIt();

        void updateProgressString(String string, Object... data);

        Object makeDialog();

        void showErrorDialog(String name, String url);
    }

    public static class Downloader extends JOptionPane implements DepLoader.IDownloadDisplay {
        private JDialog container;
        private JLabel currentActivity;
        private JProgressBar progress;
        boolean stopIt;
        Thread pokeThread;

        private Box makeProgressPanel() {
            Box box = Box.createVerticalBox();
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel welcomeLabel = new JLabel("<html><b><font size='+1'>" + owner + " is setting up your minecraft environment</font></b></html>");
            box.add(welcomeLabel);
            welcomeLabel.setAlignmentY(TOP_ALIGNMENT);
            welcomeLabel = new JLabel("<html>Please wait, " + owner + " has some tasks to do before you can play</html>");
            welcomeLabel.setAlignmentY(TOP_ALIGNMENT);
            box.add(welcomeLabel);
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            currentActivity = new JLabel("Currently doing ...");
            box.add(currentActivity);
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            progress = new JProgressBar(0, 100);
            progress.setStringPainted(true);
            box.add(progress);
            box.add(Box.createRigidArea(new Dimension(0, 30)));
            return box;
        }

        @Override
        public JDialog makeDialog() {
            if (container != null)
                return container;

            setMessageType(JOptionPane.INFORMATION_MESSAGE);
            setMessage(makeProgressPanel());
            setOptions(new Object[]{"Stop"});
            addPropertyChangeListener(evt -> {
                if (evt.getSource() == Downloader.this && evt.getPropertyName().equals(VALUE_PROPERTY)) {
                    requestClose("This will stop minecraft from launching\nAre you sure you want to do this?");
                }
            });
            container = new JDialog(null, "Hello", ModalityType.MODELESS);
            container.setResizable(false);
            container.setLocationRelativeTo(null);
            container.add(this);
            this.updateUI();
            container.pack();
            container.setMinimumSize(container.getPreferredSize());
            container.setVisible(true);
            container.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            container.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    requestClose("Closing this window will stop minecraft from launching\nAre you sure you wish to do this?");
                }
            });
            return container;
        }

        protected void requestClose(String message) {
            int shouldClose = JOptionPane.showConfirmDialog(container, message, "Are you sure you want to stop?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (shouldClose == JOptionPane.YES_OPTION)
                container.dispose();

            stopIt = true;
            if (pokeThread != null)
                pokeThread.interrupt();
        }

        @Override
        public void updateProgressString(String progressUpdate, Object... data) {
            if (currentActivity != null)
                currentActivity.setText(String.format(progressUpdate, data));
        }

        @Override
        public void resetProgress(int sizeGuess) {
            if (progress != null)
                progress.getModel().setRangeProperties(0, 0, 0, sizeGuess, false);
        }

        @Override
        public void updateProgress(int fullLength) {
            if (progress != null)
                progress.getModel().setValue(fullLength);
        }

        @Override
        public void setPokeThread(Thread currentThread) {
            this.pokeThread = currentThread;
        }

        @Override
        public boolean shouldStopIt() {
            return stopIt;
        }

        @Override
        public void showErrorDialog(String name, String url) {
            JEditorPane ep = new JEditorPane("text/html",
                    "<html>" +
                            owner + " was unable to download required library " + name +
                            "<br>Check your internet connection and try restarting or download it manually from" +
                            "<br><a href=\"" + url + "\">" + url + "</a> and put it in your mods folder" +
                            "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);
            ep.addHyperlinkListener(event -> {
                try {
                    if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                        Desktop.getDesktop().browse(event.getURL().toURI());
                } catch (Exception ignored) {
                }
            });

            JOptionPane.showMessageDialog(null, ep, "A download error has occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static class DummyDownloader implements DepLoader.IDownloadDisplay {
        @Override
        public void resetProgress(int sizeGuess) {
        }

        @Override
        public void setPokeThread(Thread currentThread) {
        }

        @Override
        public void updateProgress(int fullLength) {
        }

        @Override
        public boolean shouldStopIt() {
            return false;
        }

        @Override
        public void updateProgressString(String string, Object... data) {
        }

        @Override
        public Object makeDialog() {
            return null;
        }

        @Override
        public void showErrorDialog(String name, String url) {
        }
    }

    public static class VersionedFile {
        public final Pattern pattern;
        public final String filename;
        public final ComparableVersion version;
        public final String name;

        public VersionedFile(String filename, Pattern pattern) {
            this.pattern = pattern;
            this.filename = filename;
            Matcher m = pattern.matcher(filename);
            if (m.matches()) {
                name = m.group(1);
                version = new ComparableVersion(m.group(2));
            } else {
                name = null;
                version = null;
            }
        }

        public boolean unmatchedByRegex() {
            return name == null;
        }
    }

    public static class Dependency {
        public String url;
        public DepLoader.VersionedFile file;

        public String existing;
        /**
         * Flag set to add this dep to the classpath immediately because it is required for a coremod.
         */
        public boolean coreLib;

        public Dependency(String url, DepLoader.VersionedFile file, boolean coreLib) {
            this.url = url;
            this.file = file;
            this.coreLib = coreLib;
        }
    }

    public static class DepLoadInst {
        private final File modsDir;
        private DepLoader.IDownloadDisplay downloadMonitor;
        private JDialog popupWindow;

        private final Map<String, DepLoader.Dependency> depMap = new HashMap<>();
        private final HashSet<String> depSet = new HashSet<>();

        public DepLoadInst() {
            //String mcVer = (String) FMLInjectionData.data()[4];
            File mcDir = (File) FMLInjectionData.data()[6];

            modsDir = new File(mcDir, "mods");
            if (!modsDir.exists() && !modsDir.mkdir()) BWPConstants.L.warn("Why is there no \"mods\" directory?");
        }

        private void addClasspath(String name) {
            try {
                ((LaunchClassLoader) DepLoader.class.getClassLoader()).addURL(new File(modsDir, name).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        private void deleteMod(File mod) {
            if (mod.delete())
                return;

            if (!mod.delete()) {
                mod.deleteOnExit();
                String msg = owner + " was unable to delete file " + mod.getPath() + " the game will now try to delete it on exit. If this dialog appears again, delete it manually.";
                System.err.println(msg);
                if (!GraphicsEnvironment.isHeadless())
                    JOptionPane.showMessageDialog(null, msg, "An update error has occured", JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }
        }

        private void download(DepLoader.Dependency dep) {
            popupWindow = (JDialog) downloadMonitor.makeDialog();
            File libFile = new File(modsDir, dep.file.filename);
            try {
                URL libDownload = new URL(dep.url + '/' + dep.file.filename);
                downloadMonitor.updateProgressString("Downloading file %s", libDownload.toString());
                System.out.format("Downloading file %s\n", libDownload);
                URLConnection connection = libDownload.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("User-Agent", owner + " Downloader");
                int sizeGuess = connection.getContentLength();
                download(connection.getInputStream(), sizeGuess, libFile);
                downloadMonitor.updateProgressString("Download complete");
                System.out.println("Download complete");

                scanDepInfo(libFile);
            } catch (Exception e) {
                libFile.delete();
                if (downloadMonitor.shouldStopIt()) {
                    System.err.println("You have stopped the downloading operation before it could complete");
                    System.exit(1);
                    return;
                }
                downloadMonitor.showErrorDialog(dep.file.filename, dep.url + '/' + dep.file.filename);
                throw new RuntimeException("A download error occured", e);
            }
        }

        private void download(InputStream is, int sizeGuess, File target) throws Exception {
            if (sizeGuess > downloadBuffer.capacity())
                throw new Exception(String.format("The file %s is too large to be downloaded by " + owner + " - the download is invalid", target.getName()));

            downloadBuffer.clear();

            int bytesRead, fullLength = 0;

            downloadMonitor.resetProgress(sizeGuess);
            try {
                downloadMonitor.setPokeThread(Thread.currentThread());
                byte[] smallBuffer = new byte[1024];
                while ((bytesRead = is.read(smallBuffer)) >= 0) {
                    downloadBuffer.put(smallBuffer, 0, bytesRead);
                    fullLength += bytesRead;
                    if (downloadMonitor.shouldStopIt()) {
                        break;
                    }
                    downloadMonitor.updateProgress(fullLength);
                }
                is.close();
                downloadMonitor.setPokeThread(null);
                downloadBuffer.limit(fullLength);
                downloadBuffer.position(0);
            } catch (InterruptedIOException e) {
                // We were interrupted by the stop button. We're stopping now.. clear interruption flag.
                Thread.interrupted();
                throw new Exception("Stop");
            } catch (IOException e) {
                throw e;
            }

            try {
                /*String cksum = generateChecksum(downloadBuffer);
                if (cksum.equals(validationHash))
                {*/
                if (!target.exists())
                    target.createNewFile();


                downloadBuffer.position(0);
                FileOutputStream fos = new FileOutputStream(target);
                fos.getChannel().write(downloadBuffer);
                fos.close();
                /*}
                else
                {
                    throw new RuntimeException(String.format("The downloaded file %s has an invalid checksum %s (expecting %s). The download did not succeed correctly and the file has been deleted. Please try launching again.", target.getName(), cksum, validationHash));
                }*/
            } catch (Exception e) {
                throw e;
            }
        }

        private String checkExisting(DepLoader.Dependency dep) {
            for (File f : modsDir.listFiles()) {
                DepLoader.VersionedFile vfile = new DepLoader.VersionedFile(f.getName(), dep.file.pattern);
                if (vfile.filename.equals("Better With Mods 1.7.10 0.6.2 Beta.jar")) {
                    return f.getName(); //no need to redownload
                }
                if (vfile.unmatchedByRegex() || !vfile.name.equals(dep.file.name))
                    continue;
                return f.getName();//found dependency
            }
            return null;
        }

        public void load() {
            scanDepInfos();
            if (depMap.isEmpty())
                return;

            loadDeps();
            activateDeps();
        }

        private void activateDeps() {
            for (DepLoader.Dependency dep : depMap.values())
                if (dep.coreLib)
                    addClasspath(dep.existing);
        }

        private void loadDeps() {
            downloadMonitor = FMLLaunchHandler.side().isClient() ? new DepLoader.Downloader() : new DepLoader.DummyDownloader();
            try {
                while (!depSet.isEmpty()) {
                    Iterator<String> it = depSet.iterator();
                    DepLoader.Dependency dep = depMap.get(it.next());
                    it.remove();
                    load(dep);
                }
            } finally {
                if (popupWindow != null) {
                    popupWindow.setVisible(false);
                    popupWindow.dispose();
                }
            }
        }

        private void load(DepLoader.Dependency dep) {
            dep.existing = checkExisting(dep);
            if (dep.existing == null)//download dep
            {
                download(dep);
                dep.existing = dep.file.filename;
            }
        }

        private List<File> modFiles() {
            return new LinkedList<>(Arrays.asList(modsDir.listFiles()));
        }

        private void scanDepInfos() {
            for (File file : modFiles()) {
                if (!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip"))
                    continue;

                scanDepInfo(file);
            }
        }

        private void scanDepInfo(File file) {
            try {
                ZipFile zip = new ZipFile(file);
                ZipEntry e = zip.getEntry("betterwithmods.info");
                if (e != null)
                    loadJSon(zip.getInputStream(e));
                zip.close();
            } catch (Exception e) {
                System.err.println("Failed to load betterwithmods.info from " + file.getName() + " as JSON");
                e.printStackTrace();
            }
        }

        private void loadJSon(InputStream input) throws IOException {
            InputStreamReader reader = new InputStreamReader(input);
            JsonElement root = new JsonParser().parse(reader);
            if (root.isJsonArray())
                loadJSonArr(root);
            else
                loadJson(root.getAsJsonObject());
            reader.close();
        }

        private void loadJSonArr(JsonElement root) throws IOException {
            for (JsonElement node : root.getAsJsonArray())
                loadJson(node.getAsJsonObject());
        }

        private void loadJson(JsonObject node) throws IOException {
            boolean obfuscated = ((LaunchClassLoader) DepLoader.class.getClassLoader())
                    .getClassBytes("net.minecraft.world.World") == null;

            String testClass = node.get("class").getAsString();
            if (DepLoader.class.getResource("/" + testClass.replace('.', '/') + ".class") != null)
                return;

            String repo = node.get("repo").getAsString();
            String filename = node.get("file").getAsString();
            if (!obfuscated && node.has("dev"))
                filename = node.get("dev").getAsString();

            boolean coreLib = node.has("coreLib") && node.get("coreLib").getAsBoolean();

            Pattern pattern = null;
            try {
                if (node.has("pattern"))
                    pattern = Pattern.compile(node.get("pattern").getAsString());
            } catch (PatternSyntaxException e) {
                System.err.println("Invalid filename pattern: " + node.get("pattern"));
                e.printStackTrace();
            }
            if (pattern == null)
                pattern = Pattern.compile("(\\w+).*?([\\d.]+)[-\\w]*\\.\\D+");

            DepLoader.VersionedFile file = new DepLoader.VersionedFile(filename, pattern);
            if (file.unmatchedByRegex())
                throw new RuntimeException("Invalid filename format for dependency: " + filename);

            addDep(new DepLoader.Dependency(repo, file, coreLib));
        }

        private void addDep(DepLoader.Dependency newDep) {
            if (mergeNew(depMap.get(newDep.file.name), newDep)) {
                depMap.put(newDep.file.name, newDep);
                depSet.add(newDep.file.name);
            }
        }

        private boolean mergeNew(DepLoader.Dependency oldDep, DepLoader.Dependency newDep) {
            if (oldDep == null)
                return true;

            DepLoader.Dependency newest = newDep.file.version.compareTo(oldDep.file.version) > 0 ? newDep : oldDep;
            newest.coreLib = newDep.coreLib || oldDep.coreLib;

            return newest == newDep;
        }
    }

    public static void load() {
        if (inst == null) {
            inst = new DepLoader.DepLoadInst();
            inst.load();
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public Void call() {
        load();

        return null;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
