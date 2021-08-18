package chineseframe;
import java.io.*;

public class 输出流 extends FileInputStream
{
	public 输出流(String 路径) throws FileNotFoundException{
		super(路径);
	}
	public 输出流(File 路径) throws FileNotFoundException{
		super(路径);
	}
}
