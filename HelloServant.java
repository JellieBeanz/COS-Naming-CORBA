import HelloNaming.*;

class HelloServant extends _HelloImplBase
{
    public String sayHello()
    {
		System.out.println("Received a call.");
		return "\nShaneONeill@live.ie\n";
    }
}
