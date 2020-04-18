<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);//"localhost","my_user","my_password","my_db"
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

$ac= $_POST['myac'];
 //$ac = "qweqwe";

$sql = "SELECT `p_email` FROM `member_table`";
$result =  mysqli_query($conn, $sql); //$con, sql
while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
      }
$a = count($output);
//輸出方式
//print_r ($output[$a-1]);
//print_r ($output[0]['p_email']);

for($i =0 ; $i <  $a ; $i++){
  if( $output[$i]['p_email']  == $ac){
    echo "帳號已被註冊過";
    //echo $i;
    //echo $output[$i]['p_email'];
    return;
  }
}
//echo"拉拉";

mysqli_close($conn);

?>
