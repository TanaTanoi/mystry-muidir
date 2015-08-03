package cluedoPieces;


public class RoomCard implements Card{
	RoomType type;

	public RoomCard(RoomType type){
		this.type = type;
	}
	public RoomCard(String room){
		try {
			type = RoomType.valueOf(room.toUpperCase());
		}catch(IllegalArgumentException e){}
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof RoomCard){
			RoomCard c = (RoomCard)o;
			return type == c.type;
		}else{
			return false;
		}

	}

	@Override
	public String toString(){
		if (type != null) return type.toString();
		else return null;
	}

	@Override
	public int hashCode(){
		return type.toString().hashCode();
	}
}
