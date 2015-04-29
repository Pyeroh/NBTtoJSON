package pyeroh.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jnbt.NBTInputStream;
import org.jnbt.Tag;
import org.json.JSONObject;

public class EntryPoint {

	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.out.println("Parameters : <NBT file path> <JSON file path to create>");
			return;
		}

		File nbt = new File(System.getProperty("user.dir") + "\\" + args[0]);
		File json = new File(System.getProperty("user.dir") + "\\" + args[1]);

		System.out.println("File to convert : " + nbt.getPath());
		System.out.println("File to output : " + json.getPath());

		if (!nbt.exists()) {
			System.out.println("Input file does not exist ! Can't continue");
			return;
		}

		NBTInputStream nbtIS = new NBTInputStream(new FileInputStream(nbt));

		Tag tag = nbtIS.readTag();
		nbtIS.close();

		System.out.println("Converting...");
		JSONObject jsonObj = NBTParser.parseToJSON(tag);
		FileWriter fileWriter = new FileWriter(json);

		fileWriter.write(jsonObj.toString());
		fileWriter.close();

		System.out.println("Conversion ended succesfully");

	}

}
