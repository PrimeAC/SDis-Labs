package ttt;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TTTServer {
	public static void main(String args[]){
		int registryPort = 50000;
        System.out.println("Main OK");
        try{
            TTTService aTTTService = new TTT();
            System.out.println("After create");
            
            Registry reg = LocateRegistry.createRegistry(registryPort);
			reg.rebind("TTTService", aTTTService);
			
			//Alternativa mais realista seria ter um RMI Registry autï¿½nomo
			//disponï¿½vel no porto default (implica definir "codebase" para
			//permitir ao RMI Registry obter remotamente a interface dos
			//objectos que sejam registados):
			//
			//Naming.rebind("ShapeList", aShapelist);
           
            System.out.println("TTTService server ready");
        }catch(Exception e) {
            System.out.println("TTTService server main " + e.getMessage());
        }
    }
}
