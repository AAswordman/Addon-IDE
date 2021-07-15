package chineseframe;
import java.io.File;
import java.io.*;
import java.nio.channels.*;
import android.content.*;
import android.net.*;
import java.util.zip.*;
import bms.helper.app.EXPHelper;
import bms.helper.tools.LOG;

public class CFile {
	public File file;
	public CFile(String lj) {
		file = new File(lj);
	}
    public CFile(File f) {
        file = f;
	}



	public boolean delete() {

        return file.delete();
    }
	public void deleteDir() {
        deleteDir(this.file);
    }
	private void deleteDir(File file2) {
        File file3 = file2;
        if (!file3.exists()) {
            return;
        }
        if (file3.isDirectory()) {
            File[] listFiles = file3.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                File file4 = listFiles[i];
                if (file4.isDirectory()) {
                    deleteDir(file4);
                } else {
                    file4.delete();
                }
            }
            file3.delete();
            return;
        }
        file3.delete();
    }
	public boolean write(String 内容) throws IOException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
        boolean isSuccess = false;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bufferedWriter.write(内容);
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bufferedWriter.close();
        }

        return isSuccess;
    }
	public  String read() throws IOException {
        File file = this.file;
        BufferedReader bufferedReader = null;
        StringBuilder str = new StringBuilder();
        try {
            if (file.exists()) {
                bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String s;
                boolean first=false;
                while ((s = bufferedReader.readLine()) != null) {
                    if (first) {
                        str.append("\n").append(s);
                    } else {
                        first = true;
                        str.append(s);
                    }
                }
            }
        } catch (IOException e) {
            LOG.print(EXPHelper.get(e));
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return str.toString();
    }
	public void copy(CFile 文件) throws FileNotFoundException {
		OutputStream out=new FileOutputStream(file);
        try {
            byte[] b = new byte[2 * 1024 * 1024]; //2M memory
            int len = -1;
            while ((len = new FileInputStream(file).read(b)) > 0) {
                out.write(b, 0, len);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(out);
        }
    }
	public void fastCopy(CFile out) {
        FileChannel filein = null;
        FileChannel fileout = null;
        try {
            filein = new FileInputStream(this.file).getChannel();
            fileout = new FileOutputStream(out.file).getChannel();
            filein.transferTo(0, filein.size(), fileout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(filein, fileout);
        }
    }
	public static void shareFile(Context 活动, String 标题, String 路径) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.parse("file://" + 路径);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        活动.startActivity(Intent.createChooser(intent, 路径));

    }
	public void toZip(CFile 文件) {
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(new FileOutputStream(文件.file));
            byte[] buf = new byte[1024];
            int len;
            while ((len = new FileInputStream(file).read(buf)) != -1) {
                gzip.write(buf, 0, len);
                gzip.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(gzip);
        }
    }
	public void fromZip(CFile 文件) throws FileNotFoundException {
		OutputStream os=new FileOutputStream(文件.file);
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024];
            int len;
            while ((len = gzip.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(gzip, os);
        }
    }
	public 输出流 getOutputStream() throws FileNotFoundException {
		return new 输出流(file);
	}

	private void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
