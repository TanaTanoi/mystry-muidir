package cluedoPieces;

public class WeaponCard implements Card {
	
	WeaponType type;
	
	public WeaponCard(WeaponType type){
		this.type = type;
		
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o instanceof WeaponCard){
			WeaponCard c = (WeaponCard)o;
			return type == c.type;
		}else{
			return false;
		}
		
	}
	
	@Override
	public int hashCode(){
		return type.toString().hashCode();
	}
	
}
