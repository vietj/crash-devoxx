import org.crsh.command.CRaSHCommand
import org.crsh.cmdline.annotations.Usage
import org.crsh.cmdline.annotations.Required
import org.crsh.cmdline.annotations.Command
import org.crsh.cmdline.annotations.Argument

@Usage("lucene integration")
class lucene extends CRaSHCommand {
  @Usage("init a lucene directory")
  @Command
  Object init(@Usage("Sleep time in seconds") @Argument @Required String path) throws ScriptException {
    return "should init $path";
  }
}