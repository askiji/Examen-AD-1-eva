package examenAD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Principal {
	
		public static String url = "jdbc:sqlite:D:/aclipse/examenAD/src/Source/Practica1.db";
		public static int fecha = 010101;
		public static void main(String[] args) throws SQLException {
			// TODO Auto-generated method stub
			String valoresClientes = "ID,Nombre,Direccion,Poblacion,Telefono,NIF";
			ArrayList<String> clientes = leerCSV("D:/aclipse/examenAD/src/Source/CLIENTE.csv");
			String valoresProducto = "";
			ArrayList<String> producto = leerCSV("D:/aclipse/examenAD/src/Source/PRODUCTOS.csv");
//			rellenarTablas(clientes,valoresClientes,"clientes");
//			rellenarTablas(producto, valoresProducto,"Productos");
			insertarVentas();
	}
		
		private static <T> void insertarVentas() throws SQLException {
			Scanner sc = new Scanner(System.in);
			System.out.println("Inserte ID de la venta");
			int idVenta = Integer.valueOf(sc.nextLine());
			System.out.println("Inserte identificador cliente");
			int idCliente = Integer.valueOf(sc.nextLine());
			System.out.println("Inserte id del producto");
			int idProducto = Integer.valueOf(sc.nextLine());
			System.out.println("Inserte catidad");
			int cantidad = Integer.valueOf(sc.nextLine());
			if(consultar(idVenta, "idventas","ventas")) {
				System.out.println("Ya hay una venta con ese ID");
				return;
			}
			if(!consultar(idCliente,"ID", "clientes")) {
				System.out.println("No exite el identificador del cliente");
				return;
			}
			if(consultarCantidad()>0) {	
				System.out.println("No queda stock");
				return;
			}
			if(!consultar(idProducto , "ID" , "Productos")) {
				return;
			}
			else {
				ArrayList<T> frase = new ArrayList(Arrays.asList(idVenta+","+fecha+","+idCliente+","+idProducto+","+cantidad));
				rellenarTablas(frase,"IDventas,IDCliente,IDProducto ,Cantidad , FechaVentas","ventas");
			}
		}

		private static boolean consultar(int dato, String atributo , String table) {
			try {
				Connection conexion;
				conexion = DriverManager.getConnection(url);
				Statement sentencia = conexion.createStatement();
				ResultSet resultado = sentencia.executeQuery("SELECT "+ atributo +" from "+ table+" where "+atributo +"="+ dato );
				if(resultado.next() == true) {
					System.out.println("SELECT "+ atributo +" from "+ table+" where "+atributo +"="+ dato );
					System.out.println("pasa por el resultado");
					conexion.close();
					sentencia.close();
					resultado.close();
					return true;
				}
				else {
					conexion.close();
					sentencia.close();
					resultado.close();
					return false;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
			
		}



		private static int consultarCantidad() {
			// TODO Auto-generated method stub
			return 0;
		}


		public static ArrayList<String> leerCSV(String  directorioCSV) {
			ArrayList<String> aux = new ArrayList<>();
			try {
				// Abrimos el fichero "saludo.txt"
				FileReader fr = new FileReader(directorioCSV);

				BufferedReader bf = new BufferedReader(fr);
				String cad;
				while ((cad = bf.readLine()) != null) {
//					System.out.println(cad);
					aux.add(cad);
//					System.out.println(aux);
				}
				bf.close(); // Cerramos el fichero
			} catch (Exception e) {
				System.out.println("Error de lectura del fichero");
			}
			return aux;
		}
		
		
		public static <T> void rellenarTablas(ArrayList<T> documento ,String atributos ,String tabla) throws SQLException {
			for (T string : documento) {
			try
			{
			
				Connection conexion;
				conexion = DriverManager.getConnection(url);
				String valores[]  = ((String) string).split(";");
				String frase = "";
				if (tabla.equals("clientes")) {
					for (int i = 0; i < valores.length; i++) {
						if (i == 0 || i == 4) {
							frase += valores[i] + ",";
						}
						if (i >= 1 && i <= 3 || i == 5) {
							frase += "\"" + valores[i] + "\",";
						}

					} 
				}
				if (tabla.equals("Productos")) {
					for (int i = 0; i < valores.length; i++) {
//						if (i == 0 || i == 4) {
//						}
						if (i == 1) {
							frase += "\"" + valores[i] + "\",";
						}
						else if(i == valores.length) {
							
						}
						else {
							frase += "'"+valores[i] + "',";
						}
					}
				}
				else {
					frase = (String) string;
					frase+="a";
					System.out.println("FRASE ANTES ");
					System.out.println(frase);
				}
				Statement sentencia = conexion.createStatement();
//				System.out.println(string);
				System.out.println("INSERT INTO "+tabla+ " VALUES("+ frase.substring(0,frase.length()-1) +");" );
				sentencia.executeUpdate("INSERT INTO "+tabla+ " VALUES("+ frase.substring(0,frase.length()-1) +");"  );
				sentencia.close();
				conexion.close();
			}
				
				catch(Exception e)
				{
			}
			}

		}
}
