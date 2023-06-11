package net.md_5.bungee;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BungeeCordLauncher
{

    public static void main(String[] args) throws Exception
    {
        Security.setProperty( "networkaddress.cache.ttl", "30" );
        Security.setProperty( "networkaddress.cache.negative.ttl", "10" );
        // For JDK9+ we force-enable multi-release jar file support #3087
        if ( System.getProperty( "jdk.util.jar.enableMultiRelease" ) == null )
        {
            System.setProperty( "jdk.util.jar.enableMultiRelease", "force" );
        }

        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.acceptsAll( Arrays.asList( "help" ), "获取帮助" );
        parser.acceptsAll( Arrays.asList( "v", "version" ), "打印版本并退出" );
        parser.acceptsAll( Arrays.asList( "noconsole" ), "禁用控制台输入" );

        OptionSet options = parser.parse( args );

        if ( options.has( "help" ) )
        {
            parser.printHelpOn( System.out );
            return;
        }
        if ( options.has( "version" ) )
        {
            System.out.println( BungeeCord.class.getPackage().getImplementationVersion() );
            return;
        }

        if ( BungeeCord.class.getPackage().getSpecificationVersion() != null && System.getProperty( "IReallyKnowWhatIAmDoingISwear" ) == null )
        {
            Date buildDate = new SimpleDateFormat( "yyyyMMdd" ).parse( BungeeCord.class.getPackage().getSpecificationVersion() );

            Calendar deadline = Calendar.getInstance();
            deadline.add( Calendar.WEEK_OF_YEAR, -8 );
            if ( buildDate.before( deadline.getTime() ) )
            {
                System.err.println( "*** 警告，此版本已过时 ***" );
                System.err.println( "*** 请去 http://ci.md-5.net/job/BungeeCord 下载最新构建版本更新 ***" );
                System.err.println( "*** 您将获得关于此构建的任何支持 ***" );
                System.err.println( "*** 服务器将在10秒后启动 ***" );
                Thread.sleep( TimeUnit.SECONDS.toMillis( 10 ) );
            }
        }

        BungeeCord bungee = new BungeeCord();
        ProxyServer.setInstance( bungee );
        bungee.getLogger().info( "启用BungeeCord版本 " + bungee.getVersion() );
        bungee.start();

        if ( !options.has( "noconsole" ) )
        {
            String line;
            while ( bungee.isRunning && ( line = bungee.getConsoleReader().readLine( ">" ) ) != null )
            {
                if ( !bungee.getPluginManager().dispatchCommand( ConsoleCommandSender.getInstance(), line ) )
                {
                    bungee.getConsole().sendMessage( new ComponentBuilder( "未知的命令" ).color( ChatColor.RED ).create() );
                }
            }
        }
    }
}
