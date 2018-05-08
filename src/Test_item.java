import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

public class Test_item {

	static int valueC =0;
	static int valueH =0;
	
	public static void main(String[] args) {
		

		float[][] RMatrix ;
		
		
		
		String fileName = "text1.txt";
		File file = new File(fileName);
		
		
		RMatrix = readFileData(fileName);
		
		float[][] Matrix = new float[valueC][valueH];
		for(int i=0;i<valueC;i++)
		{
			for(int j=0;j<valueH;j++)
			{
				Matrix[i][j] = RMatrix[j][i];
			}
		}
		
		ReverValue();
		
		float[][] Pre = new float[valueH][valueC];
		
		OutMatix(Matrix, valueH, valueC);
		
		// tinh gia tri trung binh tung cot
		System.out.println("Mean Rate:");
		float[] meanRating = new float[valueC];
		
		for(int j=0;j<valueC;j++)
		{
			float mean = 0;
			int count = 0;
			for(int i=0;i<valueH;i++)
			{
				if(Matrix[i][j]==-1) ;
				else 
					{
						count++;
						mean += Matrix[i][j];
					}
			}
			meanRating[j] = mean/count;
		}
		
		for(int i=0;i<valueC;i++)
			System.out.print(meanRating[i]+"\t");
		
		// tao ma tran gia tri trung binh 
		
		for(int j=0;j<valueC;j++)
		{
			for(int i=0;i<valueH;i++)
			{
				if(Matrix[i][j] == -1) Matrix[i][j]=0;
				else
				Matrix[i][j] = Matrix[i][j] - meanRating[j];
			}
		}
		
		System.out.println("\nMatrix:");
		OutMatix(Matrix, valueH, valueC);
		
		// tao ma tran nguoi dung tuong tu
		
		float[][] simUser = new float[valueC][valueC];
		
		for(int i=0;i<valueC;i++)
		{
			for(int j=0;j<valueC;j++)
			{
				simUser[i][j] = couSim(i,j,Matrix,valueH);
			}
		}
		
		System.out.println("Simmilarity User");
		OutMatix(simUser, valueC, valueC);

		

	
		copyM(Pre,Matrix);

		for(int i=0;i < valueH;i++)
		{
			for(int j=0;j< valueC;j++)
			{
				if(Matrix[i][j] == 0) 
				{
					Pre[i][j] = mathPre(i,j, Matrix, simUser, valueH, valueC);
				
				}
			}
		}	
		
		
		
		for(int j=0;j<valueC;j++)
		{
			for(int i=0;i<valueH;i++)
			{

				Pre[i][j] = Pre[i][j] + meanRating[j];
			}
		}
		
		
		
		float[][] Result = new float[valueC][valueH];
		for(int i=0;i<valueC;i++)
		{
			for(int j=0;j<valueH;j++)
			{
				Result[i][j] = Pre[j][i];
			}
		}
		ReverValue();
		System.out.println("Matrix Result");
		OutMatix(Result, valueH, valueC);
		
		
		
		
		
	}
	
	static void ReverValue()
	{
		int tmp = valueH;
		valueH = valueC;
		valueC = tmp;
	}
	static void copyM(float[][] a, float[][] b)
	{
		for(int i=0;i<valueH;i++)
		{
			for(int j=0;j<valueC;j++)
			{
				a[i][j] = b[i][j];
			}
		}
		
	}
	private static float mathPre(int i, int j, float[][] matrix, float[][] simUser, int valueH, int valueC) {
		float[] map = new float[valueC];
		float tu=0;
		float mau=0;
		
		for(int b=0;b<valueC;b++)
		{
			if(matrix[i][b] !=0)
			{
				if(simUser[j][b]==1) map[b] = -100;
				else
				map[b] = simUser[j][b];
			}
			else
				map[b] = -100;

		}

		Arrays.sort(map);


		for(int c=1;c<3;c++)
		{
				for(int b=0;b<valueC;b++)
				{
					if(simUser[j][b] == map[valueC-c])
					{						
						tu += matrix[i][b]*simUser[j][b];
						mau += Math.abs(simUser[j][b]);
					}
				}
				
		}
		
		
		return tu/mau;
	}



	
	static float couSim(int a, int b, float[][] M, int valueH)
	{
		float result = 0;
		float tu = 0;
		float mau1 = 0;
		float mau2 = 0;
		
		for(int i=0;i<valueH;i++)
		{
			tu += M[i][a]* M[i][b];
			mau1 += Math.pow(M[i][a],2);
			mau2 += Math.pow(M[i][b],2);
		}
		
	
		result = (float) (tu/(Math.sqrt(mau1)*Math.sqrt(mau2)));
		return result;
	}
	
	static float[][] readFileData(String fileName) {
		File file = new File(fileName);
		String result = "";
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			try {

				while ((line = br.readLine()) != null) {
					result = result + line + "\n";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Matrix:\n" + result);
		String[] data = result.split("\n");
		
		valueH = data.length;
		valueC = ((data[0]).split("\\s")).length;
		float[][] matrix = new float[valueH][valueC];
		for (int i = 0; i < valueH; i++) {
			String[] stA = data[i].split("\\s");
			for (int j = 0; j < valueC; j++) {
				matrix[i][j] = Float.parseFloat(stA[j]) ;
			}
		}
		return matrix;
	}


	static void OutMatix(float[][] Matrix, int valueH, int valueC)
	{
		System.out.println("\n");
	     DecimalFormat f = new DecimalFormat("0.00");
		
		for(int i=0;i<valueH;i++) {
			for(int j=0;j<valueC;j++)
			{
				if(Matrix[i][j] == -1 ) System.out.print("\t");
				else {
					System.out.print(f.format(Matrix[i][j])+"\t");
				}
			}
		System.out.println("\n");   
			
		}
	}
}



