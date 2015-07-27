package cluedoPieces;

public class RoomCard implements Card{
	RoomType type;

	public RoomCard(RoomType type){
		this.type = type;
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
	public int hashCode(){
		return type.toString().hashCode();
	}
}
