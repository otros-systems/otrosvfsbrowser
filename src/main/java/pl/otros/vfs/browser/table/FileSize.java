/*
 * Copyright 2012 Krzysztof Otrebski (krzysztof.otrebski@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pl.otros.vfs.browser.table;

public class FileSize implements Comparable<FileSize> {

  private static final long K = 1024;
  private static final long M = K * K;
  private static final long G = M * K;
  private static final long T = G * K;

  private long bytes;

  public FileSize(long bytes) {
    super();
    this.bytes = bytes;
  }

  public long getBytes() {
    return bytes;
  }

  public void setBytes(long bytes) {
    this.bytes = bytes;
  }

  @Override
  public String toString() {
    return convertToStringRepresentation(bytes);
  }


  public static String convertToStringRepresentation(final long value) {
    final long[] dividers = new long[]{T, G, M, K, 1};
    final String[] units = new String[]{"TB", "GB", "MB", "KB", "B"};
    if (value == 0) {
      return format(0, 1, "B");
    } else if (value < 1) {
      return "Folder";
    }
    String result = null;
    for (int i = 0; i < dividers.length; i++) {
      final long divider = dividers[i];
      if (value >= divider) {
        result = format(value, divider, units[i]);
        break;
      }
    }
    return result;
  }

  private static String format(final long value,
                               final long divider,
                               final String unit) {
    final double result =
        divider > 1 ? (double) value / (double) divider : (double) value;
    return String.format("%.1f %s", Double.valueOf(result), unit);
  }

  @Override
  public int compareTo(FileSize o) {
    int result = 0;
    if (o == null || bytes > o.bytes) {
      result = 1;
    } else if (bytes < o.bytes) {
      result = -1;
    } else {
      result = 0;
    }
    return result;
  }


}
