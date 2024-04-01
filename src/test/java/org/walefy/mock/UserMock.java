package org.walefy.mock;

import java.util.HashMap;
import java.util.Map;

public class UserMock extends HashMap<String, Object> {
  public <K, V> UserMock() {
    super();
  }

  public UserMock(Map<String, Object> source) {
    super(source);
  }
}
