package msg;

import static msg.Definition.HEAD_EXIT;
import static msg.Definition.HEAD_FAIL;
import static msg.Definition.HEAD_JOIN;
import static msg.Definition.HEAD_REQUEST;
import static msg.Definition.HEAD_SCRIPT;
import static msg.Definition.HEAD_SEND;
import static msg.Definition.HEAD_SET;
import static msg.Definition.HEAD_SUCCESS;

public enum HEAD {
	SEND(HEAD_SEND), REQUEST(HEAD_REQUEST), SET(HEAD_SET), SUCCESS(HEAD_SUCCESS),
	FAIL(HEAD_FAIL), JOIN(HEAD_JOIN), EXIT(HEAD_EXIT), SCRIPT(HEAD_SCRIPT);
	
	String str;

	HEAD(String str) {
		this.str = str;
	}

	public String toString() {
		return str;
	}
}
