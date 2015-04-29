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

		File nbt = new File(System.getProperty("user.dir") + "\\src\\" + args[0]);
		NBTInputStream nbtIS = new NBTInputStream(new FileInputStream(nbt));

		Tag tag = nbtIS.readTag();
		nbtIS.close();

		System.out.println(tag.getName());
		JSONObject jsonObj = NBTParser.parseToJSON(tag);
		FileWriter fileWriter = new FileWriter(new File(System.getProperty("user.dir") + "\\src\\" + args[1]));

		fileWriter.write(jsonObj.toString());
		fileWriter.close();

	}

}
