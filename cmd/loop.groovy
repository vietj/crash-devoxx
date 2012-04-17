import org.crsh.command.CRaSHCommand
import org.crsh.cmdline.annotations.Usage
import org.crsh.cmdline.annotations.Required
import org.crsh.cmdline.annotations.Command
import org.crsh.cmdline.annotations.Argument
import org.crsh.command.ScriptException
import org.crsh.cmdline.completers.*

import org.apache.lucene.store.*;
import org.apache.lucene.index.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;

import java.io.*;

@Usage("lucene integration")
class loop extends CRaSHCommand {
  @Usage("loop until interrupted")
  @Command
  Object main() throws ScriptException {
    int i = 10;
    while (i-- > 0) {
      try {
        System.out.println("About to wait: " + i);
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("Interruped");
        return "Interrupted";
      }
    }
    return "OK men";
  }
}