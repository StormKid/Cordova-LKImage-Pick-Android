package pick.image.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ke_li on 2017/11/7.
 */

public class Utils {


    /**
     * Activity查找id
     * @param t
     * @param activity
     * @param id
     * @param <T>
     * @return
     */
    public  static <T>T findViewById(Class<T> t, Activity activity, @IdRes int id){
        View view = activity.findViewById(android.R.id.content);
        return  (T)view.findViewById(id);
    }


    /**
     * view中查找id
     * @param t
     * @param id
     * @param <T>
     * @return
     */
    public  static <T>T findViewById(Class<T> t, View view, @IdRes int id){
        return  (T)view.findViewById(id);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public  static int  getWindowWidth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        return widthPixels;
    }

    /**
     * 通过地址获取图片
     * @param context
     * @param url
     * @param imageview
     */
    public static void putImg(Context context ,String url, ImageView imageview){
        Glide.with(context).asBitmap().load(url).into(imageview);
    }



    /**
     * 获取自定义颜色parse
     * @param color
     * @return
     */
    public  static   int getColorParcelable(String color){
        String match = "[a-f0-9A-F]{6}";
        String matchApache = "[a-f0-9A-F]{8}";
        if (TextUtils.isEmpty(color))return Color.TRANSPARENT;
        if (color.matches(match)||color.matches(matchApache)){
            return  Color.parseColor("#"+color);
        }else return Color.TRANSPARENT;
    }


    /**
     * 上传文件
     *
     * @param actionURL 上次的目标路径地址
     * @param filePaths 上传的文件路径数组
     * @return 服务器响应数据
     */
    public static String uploadFile(String actionURL, String[] filePaths) {

        String towHyphens = "--";   // 定义连接字符串
        String boundary = "******"; // 定义分界线字符串
        String end = "\r\n";    //定义结束换行字符串
        try {
            // 创建URL对象
            URL url = new URL(actionURL);
            // 获取连接对象
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置允许输入流输入数据到本机
            urlConnection.setDoOutput(true);
            // 设置允许输出流输出数据到服务器
            urlConnection.setDoInput(true);
            // 设置不使用缓存
            urlConnection.setUseCaches(false);
            // 设置请求参数中的内容类型为multipart/form-data,设置请求内容的分割线为******
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            urlConnection.setConnectTimeout(20000);



            urlConnection.setRequestProperty("ACCESS_TOKEN","1cf9a73d-b543-4e66-8fa7-6dd4ac80a56e");


            // 从连接对象中获取输出流
            OutputStream outputStream = urlConnection.getOutputStream();
            // 实例化数据输出流对象，将输出流传入
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            // 遍历文件路径的长度,将路径数组下所有路径的文件都写到输出流中
            for (int i = 0; i < filePaths.length; i++) {
                // 取出文件路径
                String filePath = filePaths[i];
                // 获取文件名称
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                // 向数据输出流中写出分割符
                dataOutputStream.writeBytes(towHyphens + boundary + end);
                // 向数据输出流中写出文件参数名与文件名
                dataOutputStream.writeBytes("Content-Disposition:form-data;name=file;filename=" + fileName + end);
                // 向数据输出流中写出结束标志
                dataOutputStream.writeBytes(end);

                // 实例化文件输入流对象，将文件路径传入，用于将磁盘上的文件读入到内存中
                FileInputStream fileInputStream = new FileInputStream(filePath);
                // 定义缓冲区大小
                int bufferSize = 1024;
                // 定义字节数组对象，用来读取缓冲区数据
                byte[] buffer = new byte[bufferSize];
                // 定义一个整形变量，用来存放当前读取到的文件长度
                int length;
                // 循环从文件输出流中读取1024字节的数据，将每次读取的长度赋值给length变量，直到文件读取完毕，值为-1结束循环
                while ((length = fileInputStream.read(buffer)) != -1) {
                    // 向数据输出流中写出数据
                    dataOutputStream.write(buffer, 0, length);
                }
                // 每写出完成一个完整的文件流后，需要向数据输出流中写出结束标志符
                dataOutputStream.writeBytes(end);
                // 关闭文件输入流
                fileInputStream.close();

            }
            // 向数据输出流中写出分隔符
            dataOutputStream.writeBytes(towHyphens + boundary + towHyphens + end);
            // 刷新数据输出流
            dataOutputStream.flush();
            dataOutputStream.close();
            // 从连接对象中获取字节输入流
            int responseCode = urlConnection.getResponseCode();
//            InputStream inputStream = urlConnection.getInputStream();
//            // 实例化字符输入流对象，将字节流包装成字符流
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            // 创建一个输入缓冲区对象，将要输入的字符流对象传入
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

//            // 创建一个字符串对象，用来接收每次从输入缓冲区中读入的字符串
//            String line;
//            // 创建一个可变字符串对象，用来装载缓冲区对象的最终数据，使用字符串追加的方式，将响应的所有数据都保存在该对象中
//            StringBuilder stringBuilder = new StringBuilder();
//            // 使用循环逐行读取缓冲区的数据，每次循环读入一行字符串数据赋值给line字符串变量，直到读取的行为空时标识内容读取结束循环
//            while ((line = bufferedReader.readLine()) != null) {
//                // 将缓冲区读取到的数据追加到可变字符对象中
//                stringBuilder.append(line);
//            }

//            // 依次关闭打开的输入流
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();

            // 依次关闭打开的输出流
            outputStream.close();

            // 返回服务器响应的数据
            return responseCode+"";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
