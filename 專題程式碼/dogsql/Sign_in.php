<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);//"localhost","my_user","my_password","my_db"
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

$ac= $_POST['myac'];
$pwd =$_POST['mypwd'];

$sql = "SELECT * FROM `member_table` where `p_email` = '$ac'";
$result =  mysqli_query($conn, $sql); //$con, sql
while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
    }
$a = $output[0]['p_email'];
$b = $output[0]['p_pwd'];
$c = $output[0]['p_id'];
if($ac != null && $pwd != null && $a== $ac && $b == $pwd){
        echo '登入成功/';
        echo $c;
}else{
        echo '登入失敗/';
}
mysqli_close($conn);

?>
