package org.walefy.mock;

import java.util.HashMap;
import java.util.Map;

public class GenericJson extends HashMap<String, Object> {
  public <K, V> GenericJson() {
    super();
  }

  public GenericJson(Map<String, Object> source) {
    super(source);
  }
}
