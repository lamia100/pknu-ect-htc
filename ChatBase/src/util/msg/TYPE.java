package util.msg;
import static util.PacketDefinition.*;

import java.util.Comparator;

public enum TYPE {
	SEND(HEAD_TYPE_SEND), REQUEST(HEAD_TYPE_REQUEST), SET(HEAD_TYPE_SET), SUCCESS(HEAD_TYPE_SUCCESS),
	FAIL(HEAD_TYPE_FAIL), JOIN(HEAD_TYPE_JOIN), EXIT(HEAD_TYPE_EXIT), SCRIPT(HEAD_TYPE_SCRIPT),
	CAST(HEAD_CAST), CAST_BROAD(HEAD_CAST_BROAD), CAST_UNI(HEAD_CAST_UNI),
	CHANNEL(HEAD_CHANNEL),
	SEQ(HEAD_SEQ),
	NICK(HEAD_NICK),
	FAMILY(HEAD_FAMILY), FAMILY_PARENT(HEAD_FAMILY_PARENT), FAMILY_CHILD(HEAD_FAMILY_CHILD),
	IP(HEAD_IP), 
	MSG(HEAD_MSG),
	END(HEAD_END);
	
	String str = "";
	
	TYPE(String str) {
		this.str = str;
	}
	
	Comparator<TYPE> cmp=new Comparator<TYPE>() {
		
		@Override
		public int compare(TYPE o1, TYPE o2) {
			// TODO Auto-generated method stub
			return o1.str.compareToIgnoreCase(o2.str);
		}
	};
	public String toString() {
		return str;
	}
}
