<?php
header("Content-Type: application/json; charset=utf-8");
$response = array();
// include db connect class
require_once __DIR__ . '/db_connect.php';
$usuario = $_GET["user"];
$password = $_GET["password"];
// connecting to db
$db = new DB_CONNECT();
mysql_query("SET NAMES utf8");

$result = mysql_query("SELECT count(*) as cantidad
FROM usuario
WHERE usuario ='". $usuario ."' AND password = MD5('". $password ."')") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    if ($row = mysql_fetch_array($result)) {
        $cantidad = intval($row["cantidad"]);
    }
	if( $cantidad == 1)
	{
		// get all products from products table
		
		$query="SELECT u.idusuario, p.nombre as usuario
				FROM usuario u JOIN profesor p
				ON u.profesor = p.idprofesor
			WHERE usuario ='". $usuario ."' AND password = MD5('". $password ."')";
		$result = mysql_query($query) or die(mysql_error());
		 
		// check for empty result
		if (mysql_num_rows($result) > 0) {
			// looping through all results
			// products node
			$response = array();
			$rows = array();
			while ($row = mysql_fetch_array($result)) {
				// temp user array
				$product = array();
				$product["id"] = intval($row["idusuario"]);
				$product["nombre"] = $row["usuario"];
				//push single product into final response array
				array_push($response, $product);
				 //$rows[] = $row["nombre"];
			}
			// success
			//$response["success"] = 1;
		 
			// echoing JSON response
			
			//echo html_entity_decode(json_encode($response));
			echo json_encode($response);
			
			
		} else {
			// no products found
			$response["success"] = 0;
			$response["message"] = "No products found";
		 
			// echo no users JSON
			echo json_encode($response);
		}
	}
	else
		echo '[{"id":0,"nombre":"-"}]';
}
?>