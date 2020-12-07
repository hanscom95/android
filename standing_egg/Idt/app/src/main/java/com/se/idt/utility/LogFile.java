package com.se.idt.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;
/**
 * Created by moon on 2016-08-12.
 */
public class LogFile {

    public static final String TAG = "TestFileActivity";
    public static final String STRSAVEPATH = Environment.
            getExternalStorageDirectory()+"/GaitLogfile/";
    public static final String SAVEFILEPATH = "logFile.txt";
    public static void main(ArrayList<ArrayList<String>> arrayList) {
        //폴더 생성
        File dir = makeDirectory(STRSAVEPATH);
        //파일 생성
        File file = makeFile(dir, (STRSAVEPATH+SAVEFILEPATH));
        //절대 경로
        Log.i(TAG, ""+getAbsolutePath(dir));
        Log.i(TAG, ""+getAbsolutePath(file));

        //파일 쓰기
        String content = new String("가나다라마바다사아자차카타파하");
        writeFile(file , arrayList);

        //파일 읽기
        //readFile(file);

        //디렉토리 내용 얻어 오기
        String[] list = getList(dir);
        for(String s : list){
            Log.d(TAG, s);
        }
    }


    /**
     * 디렉토리 생성
     * @return dir
     */
    private static File makeDirectory(String dir_path){
        File dir = new File(dir_path);
        if (!dir.exists())
        {
            dir.mkdirs();
            Log.i( TAG , "!dir.exists" );
        }else{
            Log.i( TAG , "dir.exists" );
        }

        return dir;
    }

    /**
     * 파일 생성
     * @param dir
     * @return file
     */
    private static File makeFile(File dir, String file_path){
        File file = null;
        boolean isSuccess = false;
        if(dir.isDirectory()){
            file = new File(file_path);
            if(file!=null&&!file.exists()){
                Log.i( TAG , "!file.exists" );
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    Log.i(TAG, "파일생성 여부 = " + isSuccess);
                }
            }else{
                Log.i( TAG , "file.exists" );
            }
        }
        return file;
    }

    /**
     * (dir/file) 절대 경로 얻어오기
     * @param file
     * @return String
     */
    private static String getAbsolutePath(File file){
        return ""+file.getAbsolutePath();
    }

    /**
     * (dir/file) 삭제 하기
     * @param file
     */
    private boolean deleteFile(File file){
        boolean result;
        if(file!=null&&file.exists()){
            file.delete();
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    /**
     * 파일여부 체크 하기
     * @param file
     * @return
     */
    private boolean isFile(File file){
        boolean result;
        if(file!=null&&file.exists()&&file.isFile()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 디렉토리 여부 체크 하기
     * @param dir
     * @return
     */
    private boolean isDirectory(File dir){
        boolean result;
        if(dir!=null&&dir.isDirectory()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 파일 존재 여부 확인 하기
     * @param file
     * @return
     */
    private boolean isFileExist(File file){
        boolean result;
        if(file!=null&&file.exists()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 파일 이름 바꾸기
     * @param file
     */
    private boolean reNameFile(File file , File new_name){
        boolean result;
        if(file!=null&&file.exists()&&file.renameTo(new_name)){
            result=true;
        }else{
            result=false;
        }
        return result;
    }

    /**
     * 디렉토리에 안에 내용을 보여 준다.
     * @param file
     * @return
     */
    private static String[] getList(File dir){
        if(dir!=null&&dir.exists())
            return dir.list();
        return null;
    }

    /**
     * 파일에 내용 쓰기
     * @param file
     * @param file_content
     * @return
     */
    private static boolean writeFile(File file, ArrayList<ArrayList<String>> file_content){
        boolean result;
        FileOutputStream fos;
        if(file!=null&&file.exists()&&file_content!=null){
            try {
                fos = new FileOutputStream(file);
//                String textString = new String();
                StringBuilder textString = new StringBuilder();
                try {
                    Log.d("Logfile", "file_content.size() : " + file_content.size() + "file_content.get(i).size():" + file_content.get(0).size());
                    Log.d("Logfile", "file_content.get(0).get(1) : " + file_content.get(0).get(0) + "file_content.get(0).get(1):" + file_content.get(0).get(1) + "file_content.get(0).get(2):" + file_content.get(0).get(2));
                    for(int i = 0; i < file_content.size(); i++) {
                        Log.d("array i열",  "" + i);
//                        textString="";
                        for(int j = 0; j < file_content.get(i).size(); j++) {
                            Log.d("array j열",  j+"////" + file_content.get(i).get(j));
                            textString.append(file_content.get(i).get(j));
                            textString.append("      //     ");

//                            if(file_content.get(i).get(j).length() > 0) {
//                                textString += file_content.get(i).get(j);//IndexOutOfBoundsException error 발생 =========================
//                                textString += "    ";
//                            }
                        }
                        textString.append("\n");
//                        textString += "\n";
                    }
                    fos.write(textString.toString().getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    /**
     * 파일 읽어 오기
     * @param file
     */
    private static void readFile(File file){
        int readcount=0;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                readcount = (int)file.length();
                byte[] buffer = new byte[readcount];
                fis.read(buffer);
                for(int i=0 ; i<file.length();i++){
                    Log.d(TAG, ""+buffer[i]);
                }
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 파일 복사
     * @param file
     * @param save_file
     * @return
     */
    private static boolean copyFile(File file, String save_file){
        boolean result;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fis.read(buffer,0,1024))!= -1){
                    newfos.write(buffer,0,readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }
}
