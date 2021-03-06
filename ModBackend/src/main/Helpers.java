package main;

import java.util.Arrays;
import java.util.List;

import datatypes.Auction;

public class Helpers {
	
	private static boolean WHITELISTED = false;
	public static boolean DEVBUILD = false;
	
	public static String pruneName(Auction input) {
		
		String name = input.item_name;
		
		if(name.contains("Enchanted Book")) {
			int firstComma = input.item_lore.indexOf(',') > 0 ? input.item_lore.indexOf(',') : Integer.MAX_VALUE;
			int firstNewline = input.item_lore.indexOf('\n') > 0 ? input.item_lore.indexOf('\n') : Integer.MAX_VALUE;
			
			int end = Math.min(firstComma, firstNewline);
			int start = 2;
			while(!Character.isLetterOrDigit(input.item_lore.charAt(start))) {
				start += 2;
			}
			name = input.item_lore.substring(start, end);
		}
		
		name = name.replaceAll("[^\\x00-\\x7F]", "").trim();
		String[] reforgesArr = {"NECROTIC", "ANCIENT", "FABLED", "GIANT", "GENTLE", "ODD", "FAST", "FAIR", "EPIC", "SHARP", "HEROIC", "SPICY", "LEGENDARY", "DIRTY", "GILDED", "WARPED", "BULKY", "SALTY", "TREACHEROUS", "STIFF", "LUCKY", "DEADLY", "FINE", "GRAND", "HASTY", "NEAT", "RAPID", "UNREAL", "AWKWARD", "RICH", "PRECISE", "HEADSTRONG", "CLEAN", "FIERCE", "HEAVY", "LIGHT", "MYTHIC", "PURE", "SMART", "TITANIC", "WISE", "PERFECT", "SPIKED", "RENOWNED", "CUBIC", "WARPED", "REINFORCED", "LOVING", "RIDICULOUS", "BIZARRE", "ITCHY", "OMINOUS", "PLEASANT", "PRETTY", "SHINY", "SIMPLE", "STRANGE", "VIVID", "GODLY", "DEMONIC", "FORCEFUL", "HURTFUL", "KEEN", "STRONG", "SUPERIOR", "UNPLEASANT", "ZEALOUS", "SILKY", "BLOODY", "SHADED", "SWEET", "FRUITFUL", "MAGNETIC", "REFINED", "BLESSED", "FLEET", "STELLAR", "MITHRAIC", "AUSPICIOUS", "HEATED", "AMBERED"};
		List<String> reforges = Arrays.asList(reforgesArr);
		
		String tag = name.toUpperCase();
		
		List<String> tagSplit = Arrays.asList(tag.split(" "));
		
		if (reforges.indexOf(tagSplit.get(0)) >= 0) {
            tagSplit = tagSplit.subList(1, tagSplit.size());
        }
		
		return String.join(" ", tagSplit).toLowerCase();
	}
	
	public static String niceName(String pruned) {
		String[] s = pruned.split(" ");
		String output = "";
		for(int i = 0; i < s.length;i++) {
			s[i] = (s[i].charAt(0) + "").toUpperCase() + s[i].substring(1);
		}
		for(int i = 0; i < s.length - 1;i++) {
			output += s[i] + " ";
		}
		return output + "(" + s[s.length - 1] + ")";
	}
	
	public static String localeString(int n) {
		String s = "" + n;
		int counter = 0;
		String output = "";
		while(s.length() - counter > 0) {
			output = s.charAt(s.length() - 1 - counter) + output;
			if(counter % 3 == 2 && s.length() - counter > 1) {
				output = "," + output;
			}
			counter++;
		}
		return output;
	}
	
	public static double cleanRound(double d, int i) {
		return ((double)Math.round(d * Math.pow(10, i))) / Math.pow(10, i);
	}
}


