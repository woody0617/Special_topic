<?php
header("Content-Type:text/html; charset=utf-8");
$dbhost = 'localhost';
$dbuser = 'happydog';
$dbpass = 'happy12354';
$dbname = 'app_dogface';
$conn = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);//"localhost","my_user","my_password","my_db"
mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

//狗id
//人id
//人name
$did = $_POST['dogid'];
$pid = $_POST['myid'];
$sql = "SELECT * FROM `dog_table`  where p_id='$pid' && d_id='$did'";
$result = mysqli_query($conn,$sql) ;//查詢
while($e=mysqli_fetch_assoc($result)){
  $output[]=$e;
}
$c = $output[0]['d_variety'];
if ($c == "柯基"){
  $c ="corgi";
}else if($c == "哈士奇"){
  $c ="husky";
}else{
  $c ="shibainu";
}

$pathname = "D$did"."_$pid";

$path="C:/xampp/htdocs/docker/third_who_dog/$c/tf_files/train/$pathname";

if (is_dir($path)){
  echo "對不起，目錄已經存在！";
 }else{
  $res=mkdir(iconv("UTF-8", "GBK", $path),0777,true);
  if ($res){
   echo "目錄創建成功";
  }else{
   echo "目錄創建失敗";
  }
 }

?>
