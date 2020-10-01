package net.elzorro99.totemfactions.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import net.elzorro99.totemfactions.Main;

public class UTopData {
	
	private List<List<FactionsPoint>> list;
	
	public UTopData(){
		list = new ArrayList<>();
	}
	
	@SuppressWarnings("resource")
	public void initAll(FileConfiguration data){
		if(data == null) return;
		list.clear();
		List<FactionsPoint> bigList = new ArrayList<>();
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(Main.getInstance().fileStats));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				String factionName = line.replace(":", "");
				try {
					for(int i = 0; i < 20; i++) factionName = factionName.replace(" ", "");
				}catch(Exception e) {e.printStackTrace();}
				if(data.contains("TotemStats." + factionName)){
					int wins = data.getInt("TotemStats." + factionName + ".wins");
					bigList.add(new FactionsPoint(factionName, wins));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		List<FactionsPoint> temp = new ArrayList<>();
		temp.addAll(bigList);
		
		while(temp.size() > 0){
			List<FactionsPoint> result = new ArrayList<>();
			for(int i = 0; i < 10;i++){
				FactionsPoint best = null;
				int bestWin = 0;
				for(FactionsPoint factionsPoint : temp) {
					if(factionsPoint == null) continue;
					if(best == null) {
						best = factionsPoint;
						bestWin = factionsPoint.getValue();
						continue;
					}
					if(factionsPoint.getValue() > bestWin) {
						best = factionsPoint;
						bestWin = factionsPoint.getValue();
					}
				}
				if(best == null) continue;
				temp.remove(best);
				result.add(best);
			}
			list.add(result);
		}
	}
	
	public boolean replaceName(String a, String b){
		for(List<FactionsPoint> c: list){
			for(FactionsPoint d : c){
				if(d.getKey().equals(a)){
					d.setKey(b);
					return true;
				}
			}
		}
		return false;
	}
	
	public void removeFaction(FactionsPoint factionsPoint) {
		if(factionsPoint == null) return;
		
		int id = -1;
		int local_id = -1;
		for(int i = 0; i < list.size(); i++){
			List<FactionsPoint> local = list.get(i);
			if(local.contains(factionsPoint)){
				id = i;
				for(int j = 0; j < local.size(); j++){
					if(local.get(j).equals(factionsPoint)){
						local_id=j;
						break;
					}
				}
				break;
			}
		}
		if(id < 0 || local_id < 0) return;
		
		List<FactionsPoint> edit_local = list.get(id);
		for(int i = local_id; i < (edit_local.size()-1); i++){
			edit_local.set(i, edit_local.get(i+1));
		}
		
		for(int i = (id+1); i < list.size(); i++){
			up(i);
		}
		edit_local = list.get(list.size()-1);
		edit_local.remove(edit_local.size()-1);
	}
	
	private void up(int id){
		List<FactionsPoint> edit = list.get(id);
		List<FactionsPoint> upper = list.get(id-1);
		upper.add(upper.size()-1, edit.get(0));
		for(int i = 1; i < (edit.size()-1); i++){
			edit.set(i, edit.get(i+1));
		}
	}
	
	public void updateFaction(FactionsPoint factionsPoint) {
		if(factionsPoint == null) return;
		
		List<FactionsPoint> best = new ArrayList<>();
		
		for(List<FactionsPoint> localList : list){
			if(localList == null) continue;
			if(localList.size() < 1) continue;
			FactionsPoint localFactionsPoint = localList.get((localList.size()-1));
			if(localFactionsPoint == null) continue;
			if(factionsPoint.getValue() > localFactionsPoint.getValue()) {
				best = localList;
				break;
			}
		}
		
		if(best.size() < 1) {
			best.add(factionsPoint);
		}else {
			List<FactionsPoint> temp = new ArrayList<>();
			temp.addAll(best);
			temp.add(factionsPoint);
			List<FactionsPoint> result = new ArrayList<>();
			for(int i = 0; i < (best.size()+1);i++) {
				FactionsPoint bestFactionsPoint = null;
				int bestWin = 0;
				for(FactionsPoint localFactionsPoint : temp) {
					if(localFactionsPoint == null) continue;
					if(best == null) {
						bestFactionsPoint = localFactionsPoint;
						bestWin = bestFactionsPoint.getValue();
						continue;
					}
					if(factionsPoint.getValue() > bestWin) {
						bestFactionsPoint = localFactionsPoint;
						bestWin = bestFactionsPoint.getValue();
					}
				}
				if(bestFactionsPoint == null) continue;
				temp.remove(bestFactionsPoint);
				result.add(bestFactionsPoint);
			}
			best = result;
		}
		if(list.contains(best)) return;
		list.add(best);
	}
	
	public FactionsPoint getFactions(String name, int win){
		if(name == null) return null;
		for(List<FactionsPoint> listFactionsPoint : list) {
			if(listFactionsPoint == null) continue;
			for(FactionsPoint factionsPoint : listFactionsPoint) {
				if(factionsPoint == null) continue;
				if(factionsPoint.getKey().equals(name)){
					if(win > factionsPoint.getValue()) factionsPoint.setValue(win);
					return factionsPoint;
				}
			}
		}
		return new FactionsPoint(name, win);
	}
	
	public List<FactionsPoint> get(int page){
		if(page < 0) return new ArrayList<>();
		try {
			return list.get(page);
		}catch(Exception e) {}
		return new ArrayList<>();
	}
	
	public int size(){
		int size = 0;
		for(List<FactionsPoint> a : list) {
			size = size+a.size();
		}
		return size;
	}
	
	
	
	
	public static class FactionsPoint {
		
		private String key;
		private int value;
		
		public FactionsPoint(String key, int value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public void setKey(String key){
			this.key = key;
		}
			
		public void setValue(int value) {
			this.value = value;
		}
	}

}
