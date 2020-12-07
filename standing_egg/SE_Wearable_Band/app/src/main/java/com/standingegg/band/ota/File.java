package com.standingegg.band.ota;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.os.Environment;

import com.standingegg.band.util.ULog;

/**
 * Created by wouter on 9-10-14.
 */
public class File {
	private static String filesDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LamBand";
	private InputStream inputStream;
	private byte crc;
	private byte[] bytes;

	private byte[][][] blocks;

	private int fileBlockSize = 0;
	private int bytesAvailable;
	private int numberOfBlocks = -1;
	private int chunksPerBlockCount;
	private int totalChunkCount;
	private int type;

	private File(InputStream inputStream) throws IOException {
		this.inputStream = inputStream;
		this.bytesAvailable = this.inputStream.available();
	}

	public void setType(int type) throws IOException {
		this.type = type;

		if (type == SuotaManager.TYPE) {
			// Reserve 1 extra byte to the total array for the CRC code
			this.bytes = new byte[this.bytesAvailable + 1];
			this.inputStream.read(this.bytes);
			this.crc = getCrc();
			this.bytes[this.bytesAvailable] = this.crc;
		} else {
			this.bytes = new byte[this.bytesAvailable];
			this.inputStream.read(this.bytes);
		}
	}

	public void setFile() {
		String paths = File.class.getResource("\\").getPath(); // 현재 클래스의 절대 경로
		System.out.println(paths); // --> 절대 경로가 출력됨
		java.io.File fileInSamePackage = new java.io.File(paths + "fw_1.img"); //
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileInSamePackage);
			//byte[] data = new byte[fis.available()];
			java.io.File file = new java.io.File(filesDir + "/fw_1.img");
		    FileOutputStream fileOutput = new FileOutputStream(file);
		 
		    byte[] buffer = new byte[1024];
		    int bufferLength = 0; 
		 
		    while ( (bufferLength = fis.read(buffer)) > 0 ) {
		        fileOutput.write(buffer, 0, bufferLength);
		    }
		    fileOutput.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	};

	public int getFileBlockSize() {
		return this.fileBlockSize;
	}

	public int getNumberOfBytes() {
		return this.bytes.length;
	}

	public void setFileBlockSize(int fileBlockSize) {
		this.fileBlockSize = fileBlockSize;
		this.chunksPerBlockCount = (int) Math.ceil((double) fileBlockSize / (double) Statics.fileChunkSize);
		this.numberOfBlocks = (int) Math.ceil((double) this.bytes.length / (double) this.fileBlockSize);
		this.initBlocks();
	}

	private void initBlocksSuota() {
		int totalChunkCounter = 0;
		blocks = new byte[this.numberOfBlocks][][];
		int byteOffset = 0;
		// Loop through all the bytes and split them into pieces the size of the default chunk size
		for (int i = 0; i < this.numberOfBlocks; i++) {
			int blockSize = this.fileBlockSize;
			if (i + 1 == this.numberOfBlocks) {
				blockSize = this.bytes.length % this.fileBlockSize;
			}
			int numberOfChunksInBlock = (int) Math.ceil((double) blockSize / Statics.fileChunkSize);
			int chunkNumber = 0;
			blocks[i] = new byte[numberOfChunksInBlock][];
			for (int j = 0; j < blockSize; j += Statics.fileChunkSize) {
				// Default chunk size
				int chunkSize = Statics.fileChunkSize;
				// Last chunk of all
				if (byteOffset + Statics.fileChunkSize > this.bytes.length) {
					chunkSize = this.bytes.length - byteOffset;
				}
				// Last chunk in block
				else if (j + Statics.fileChunkSize > blockSize) {
					chunkSize = this.fileBlockSize % Statics.fileChunkSize;
				}

				ULog.d("chunk", "total bytes: " + this.bytes.length + ", offset: " + byteOffset + ", block: " + i + ", chunk: " + (chunkNumber + 1) + ", blocksize: " + blockSize + ", chunksize: " + chunkSize);
				byte[] chunk = Arrays.copyOfRange(this.bytes, byteOffset, byteOffset + chunkSize);
				blocks[i][chunkNumber] = chunk;
				byteOffset += chunkSize;
				chunkNumber++;
				totalChunkCounter++;
			}
		}
		// Keep track of the total chunks amount, this is used in the UI
		this.totalChunkCount = totalChunkCounter;
	}


	private void initBlocksSpota() {
		this.numberOfBlocks = 1;
		this.fileBlockSize = this.bytes.length;
		this.totalChunkCount = (int) Math.ceil((double) this.bytes.length / (double) Statics.fileChunkSize);
		this.blocks = new byte[numberOfBlocks][this.totalChunkCount][];
		int byteOffset = 0;
		int chunkSize = Statics.fileChunkSize;
		for (int i = 0; i < this.totalChunkCount; i++) {
			if (byteOffset + Statics.fileChunkSize > this.bytes.length) {
				chunkSize = this.bytes.length - byteOffset;
			}
			byte[] chunk = Arrays.copyOfRange(this.bytes, byteOffset, byteOffset + chunkSize);
			blocks[0][i] = chunk;
			byteOffset += Statics.fileChunkSize;
		}
	}

	// Create the array of blocks using the given block size.
	private void initBlocks() {
		if (this.type == SuotaManager.TYPE) {
			this.initBlocksSuota();
		} else if (this.type == SpotaManager.TYPE) {
			this.initBlocksSpota();
		}
	}

	public byte[][] getBlock(int index) {
		return blocks[index];
	}

	public void close() {
		if (this.inputStream != null) {
			try {
				this.inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getNumberOfBlocks() {
		return this.numberOfBlocks;
	}

	public int getChunksPerBlockCount() {
		return chunksPerBlockCount;
	}

	public int getTotalChunkCount() {
		return this.totalChunkCount;
	}

	private byte getCrc() throws IOException {
		byte crc_code = 0;
		for (int i = 0; i < this.bytesAvailable; i++) {
			Byte byteValue = this.bytes[i];
			int intVal = byteValue.intValue();
			crc_code ^= intVal;
		}
		ULog.d("crc", "crc: " + String.format("%#10x", crc_code));
		return crc_code;
	}

	public static File getByFileName(String filename) throws IOException {
		// Get the file and store it in fileStream
		InputStream is = new FileInputStream(filesDir + "/" + filename);
		return new File(is);
	}

    public static ArrayList<String> list() {
		java.io.File f = new java.io.File(filesDir);
		java.io.File file[] = f.listFiles();
        Arrays.sort(file, new Comparator<java.io.File>() {
            @Override
            public int compare(java.io.File lhs, java.io.File rhs) {
                return lhs.getPath().compareToIgnoreCase(rhs.getPath());
            }
        });
		ULog.d("Files", "Size: "+ file.length);
        ArrayList<String> names = new ArrayList<String>();
		for (int i=0; i < file.length; i++)
		{
			ULog.d("Files", "FileName:" + file[i].getName());
            names.add(file[i].getName());
		}
        return names;

//		Field[] fields = R.raw.class.getFields();
//		for (int count = 0; count < fields.length; count++) {
//			Field file = fields[count];
//			int resourceID = -1;
//			try {
//				resourceID = file.getInt(file);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//			String name = file.getName();
//			map.put(resourceID, name);
//		}
	}

//	public static InputStream get(Context c, String filename) {
//		InputStream inputStream = c.getResources().openRawResource(resourceID);
//		return inputStream;
//	}

	public static void createFileDirectories(Context c) {
		String directoryName = filesDir;
		java.io.File directory;
		directory = new java.io.File(directoryName);
		directory.mkdirs();
	}
}
