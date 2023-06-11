package net.md_5.bungee;

public class Bootstrap
{

    public static void main(String[] args) throws Exception
    {
        if ( Float.parseFloat( System.getProperty( "java.class.version" ) ) < 52.0 )
        {
            System.err.println( "*** 错误 *** BungeeCord 使用Java8! 请下载!" );
            System.out.println( "你可以用这条命令检测: java -version" );
            return;
        }

        BungeeCordLauncher.main( args );
    }
}
