package pyeroh.common;

import java.util.Iterator;
import java.util.Map;

import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.IntArrayTag;
import org.jnbt.ListTag;
import org.jnbt.NBTConstants;
import org.jnbt.NBTUtils;
import org.jnbt.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class NBTParser {

	public static JSONObject parseToJSON(Tag tag) {
		JSONObject jsonObj = new JSONObject();

		int type = getType(tag);
		switch (type) {
		case 0:
			jsonObj = new JSONObject().put(tag.getName(), parseArray(tag));
			break;
		case 1:
			jsonObj = parseSimpleType(tag);
			break;
		case 2:
			jsonObj = parseCompound((CompoundTag) tag);
			break;
		default:
			break;
		}

		return jsonObj;
	}

	private static JSONArray parseArray(Tag tag) {
		JSONArray jsonArray = new JSONArray();

		if (NBTUtils.getTypeCode(tag.getClass()) == NBTConstants.TYPE_LIST) {
			ListTag listTag = (ListTag) tag;
			for (Tag elementTag : listTag.getValue()) {
				Object valToPut;
				if (getType(elementTag) != 2) {
					valToPut = parseToJSON(elementTag).get(elementTag.getName());
				}
				else {
					valToPut = parseToJSON(elementTag);
				}
				jsonArray.put(valToPut);
			}
		}
		else if (NBTUtils.getTypeCode(tag.getClass()) == NBTConstants.TYPE_INT_ARRAY) {
			IntArrayTag intArrayTag = (IntArrayTag) tag;
			int[] value = intArrayTag.getValue();
			for (int i : value) {
				jsonArray.put(i);
			}
		}
		else {
			ByteArrayTag byteArrayTag = (ByteArrayTag) tag;
			byte[] value = byteArrayTag.getValue();
			for (byte b : value) {
				jsonArray.put(b);
			}
		}

		return jsonArray;
	}

	private static JSONObject parseSimpleType(Tag tag) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(tag.getName(), tag.getValue());
		return jsonObj;
	}

	private static JSONObject parseCompound(CompoundTag tag) {
		JSONObject jsonObj = new JSONObject();

		Map<String, Tag> valuesMap = tag.getValue();
		for (Iterator<String> iterator = valuesMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Tag value = valuesMap.get(key);
			Object valToPut;
			if (getType(value) != 2) {
				valToPut = parseToJSON(value).get(key);
			}
			else {
				valToPut = parseToJSON(value);
			}
			jsonObj.put(key, valToPut);
		}

		return jsonObj;
	}

	/**
	 * Renvoie 0 pour un tableau, 1 pour un type simple, 2 pour un objet
	 *
	 * @param tag
	 * @return
	 */
	private static int getType(Tag tag) {
		int returnType = 0;

		int type = NBTUtils.getTypeCode(tag.getClass());
		switch (type) {
		case NBTConstants.TYPE_BYTE_ARRAY:
		case NBTConstants.TYPE_INT_ARRAY:
		case NBTConstants.TYPE_LIST:
			returnType = 0;
			break;
		case NBTConstants.TYPE_BYTE:
		case NBTConstants.TYPE_SHORT:
		case NBTConstants.TYPE_INT:
		case NBTConstants.TYPE_LONG:
		case NBTConstants.TYPE_FLOAT:
		case NBTConstants.TYPE_DOUBLE:
		case NBTConstants.TYPE_STRING:
			returnType = 1;
			break;
		case NBTConstants.TYPE_COMPOUND:
			returnType = 2;
			break;
		default:
			break;
		}

		return returnType;
	}

}
