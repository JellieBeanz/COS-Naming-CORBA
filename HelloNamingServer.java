import java.io.*;
import org.omg.CORBA.*;
import HelloNaming.*;
import org.omg.CosNaming.* ;
import org.omg.CosNaming.NamingContextPackage.*;
import java.util.Properties;

public class HelloNamingServer{

	public static void main (String args[]) {
		try{

			Properties props = new Properties(); props.put("org.omg.CORBA.ORBInitialPort", "49000");

			NameComponent nc[] = new NameComponent[1];

	    	// create and initialize the ORB
	   		ORB orb = ORB.init(args, null);

			HelloServant helloRef = new HelloServant();

			//connecting the servant to the orb
			orb.connect(helloRef);
	   		System.out.println("Orb connected." + orb);

			//You need to locate the naming service. The naming serivce helps you locate other objects.
			//The CORBA orb lets you locate certain services by name. The call
			// String[] services = orb.list_initial_services();
			// System.out.println ("Listing Services");
			//lists the names of the standard services that the ORB can connect to. The Naming service
			//has the standard name NameService. To obtain an object reference to the service you use
			//resolve_initial_reference. It returns a generic CORBA object. Note you have to use
			//org.omg.CORBA.Object otherwise the compiler assumes that you mean java.lang.Object
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	   		System.out.println("Found NameService.");

			//Next you need to convert this reference into a NamingContext reference
			//so that you can invoke the methods of the NamingContext interface.
			//This is achieved by using the narrow function of the helper class
			NamingContext rootCtx = NamingContextHelper.narrow(objRef);

			//Now that we have the naming context we can use it to place a server object
			//Names are nested sequences of name components. You can use the nesting levels
			//to organize hierarchies of names much like you use directories in a file system.
			//A Name Component consists of an ID and a kind. The ID is a name for the component
			//that is unique among all names with the same parent component. The kind is some indication
			//of the type of the component. Use
			//-"Context" for name components that have nested names; and
			//-"Object" for object names

			nc[0] = new NameComponent("Trainee", "Department");
			NamingContext Hello3Ctx = rootCtx.bind_new_context(nc);
			System.out.println("Context 'Trainee' added to Departments.");
			//no Contents to this context/Department

			nc[0] = new NameComponent("Finance", "Department");
			NamingContext HelloCtx = rootCtx.bind_new_context(nc);
			System.out.println("Context 'Finance' added to Departments.");

			nc[0] = new NameComponent("John@live.com", "Object");
			//NameComponent path[] = {nc};
			//Binding the name to an object that is stored in the Naming Context
			HelloCtx.rebind(nc, helloRef);
			System.out.println("Object 'John@live.com' added to Finance Department.");

			nc[0] = new NameComponent("Sales", "Department");
			NamingContext Hello2Ctx = rootCtx.bind_new_context(nc);
			System.out.println("Context 'Sales' added to Departments.");

			nc[0] = new NameComponent("NFlanders", "User");
			NamingContext Hello4Ctx = Hello2Ctx.bind_new_context(nc);
			System.out.println("User 'NFlanders' added to Sales Department.");

			nc[0] = new NameComponent("NedFlanders@live.ie", "Object");
			Hello4Ctx.rebind(nc, Hello4Ctx);
			System.out.println("Object 'NedFlanders@live.ie' added to User Context.");

			nc[0] = new NameComponent("SONeill", "User");
			NamingContext Hello5Ctx = Hello2Ctx.bind_new_context(nc);
			System.out.println("Context 'User' added to Sales Department.");

			nc[0] = new NameComponent("ShaneONeill@live.ie", "Object");
			Hello5Ctx.rebind(nc, Hello5Ctx);
			System.out.println("Email 'ShaneONeill@live.ie' added to User.");

				// wait for invocations from clients
			orb.run();



		} catch (Exception e) {
			System.err.println("Error: "+e);
			e.printStackTrace(System.out);
		}

	}
}
