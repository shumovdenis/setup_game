import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        File dirSrc = new File("D:/Games/src");
        createDir(dirSrc, sb);

        File dirRes = new File("D:/Games/res");
        createDir(dirRes, sb);

        File dirSaveGames = new File("D:/Games/savegames");
        createDir(dirSaveGames, sb);

        File dirTemp = new File("D:/Games/temp");
        createDir(dirTemp, sb);
        File fileTemp = new File(dirTemp, "Temp.txt");
        createFile(fileTemp, sb);

        File dirTest = new File("D:/Games/src/test");
        createDir(dirTest, sb);

        File dirMain = new File("D:/Games/src/main");
        createDir(dirMain, sb);
        File fileMain = new File(dirMain, "Main.java");
        createFile(fileMain, sb);
        File fileUtils = new File(dirMain, "Utils.java");
        createFile(fileUtils, sb);

        File dirDrawabels = new File("D:/Games/res/drawabels");
        createDir(dirDrawabels, sb);

        File dirVectors = new File("D:/Games/res/vectors");
        createDir(dirVectors, sb);

        File dirIcons = new File("D:/Games/res/icons");
        createDir(dirIcons, sb);

        File fileSave_1 = new File(dirSaveGames, "save_1.dat");
        createFile(fileSave_1, sb);
        File fileSave_2 = new File(dirSaveGames, "save_2.dat");
        createFile(fileSave_2, sb);
        File fileSave_3 = new File(dirSaveGames, "save_3.dat");
        createFile(fileSave_3, sb);

        writeLog(fileTemp, sb);

        GameProgress gameProgress_1 = new GameProgress(100, 3, 1, 10);
        GameProgress gameProgress_2 = new GameProgress(77, 6, 4, 659);
        GameProgress gameProgress_3 = new GameProgress(54, 9, 6, 1375);

        writeSave(fileSave_1, gameProgress_1);
        writeSave(fileSave_2, gameProgress_2);
        writeSave(fileSave_3, gameProgress_3);

        packZip(dirSaveGames);
        deleteNonZipSaveFiles(dirSaveGames);

        unpackZip();
        openProgress();
    }

    public static void openProgress(){
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream("D:/Games/savegames/save_2.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        }
        catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(gameProgress);
    }

    public static void unpackZip(){
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream("D:/Games/savegames/save.zip"))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream("D:/Games/savegames/" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void deleteNonZipSaveFiles(File file){
        for (File item : file.listFiles()){
            if(item.getName().contains(".dat")) {
                item.delete();
            }
        }
    }

    public static void packZip(File file){
        try (FileOutputStream fos = new FileOutputStream("D:/Games/savegames/save.zip");
             ZipOutputStream zout = new ZipOutputStream(fos)){
            for(File item : file.listFiles()){
                if(item.getName().equals("save.zip")) continue;
                FileInputStream fis = new FileInputStream("D:/Games/savegames/" + item.getName());
                ZipEntry entry = new ZipEntry(item.getName());
                zout.putNextEntry(entry);
                byte [] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void writeSave(File file, GameProgress gameProgress){
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void writeLog(File file, StringBuilder sb){
        try (FileWriter writer = new FileWriter(file, false)){
            writer.write(String.valueOf(sb));
            writer.flush();
        } catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    public static void createFile(File file, StringBuilder sb){
        try{
            if (file.createNewFile())
                sb.append("Файл " + file.getName() + " создан"+ "\n");
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void createDir(File dir, StringBuilder sb){
        if(dir.mkdir()){
            sb.append("Директория " + dir.getName() + " создана" + "\n");
        }
    }
}


