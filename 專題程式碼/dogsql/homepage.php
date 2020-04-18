<?php
    header("Content-Type:text/html; charset=utf-8");
    $dbhost = 'localhost';
    $dbuser = 'happydog';
    $dbpass = 'happy12354';
    $dbname = 'app_dogface';
    $conn = mysqli_connect($dbhost, $dbuser, $dbpass,$dbname) ;//連接資料庫
    mysqli_query($conn, "SET NAMES UTF8"); //$con, sql

    $id = $_POST['pid'];
    $ac = $_POST['ac'];
    $sql = "SELECT * FROM `member_table`  where p_id='$id' && p_email='$ac'";//人名
    $result = mysqli_query($conn,$sql) ;//查詢
    while($e=mysqli_fetch_assoc($result)){
      $output[]=$e;
    }
    $a = $output[0]['p_name'];
    //echo $name;
    $sql = "SELECT * FROM `dog_table`  where p_id='$id'";//狗數量
    $result = mysqli_query($conn,$sql) ;//查詢
    $num = mysqli_num_rows($result);
    if($num > 0){
      while($e=mysqli_fetch_assoc($result)){
        $outputa[]=$e;
      }
      $b = count($outputa);
      $brandom = rand(0,count($outputa)-1);
      $dogid = $outputa[$brandom]['d_id'];//隨機抓狗id
      $dogname = $outputa[$brandom]['d_name'];//犬名
      $dogvariety = $outputa[$brandom]['d_variety'];//抓品種
      $dogvar2 = $outputa[$brandom]['d_variety'];//抓品種
      //echo $outputa[$brandom]['d_id'];
      if ($dogvariety == "柯基"){
        $dogvariety ="corgi";
      }else if($dogvariety == "哈士奇"){
        $dogvariety ="husky";
      }else{
        $dogvariety ="shibainu";
      }

      $pathname = "D$dogid"."_$id";//<--檔名
    }else{
      $b = "0";
      $dogvariety ="";
      $dogvar2 ="";
      $pathname ="";
      $dogname = "";
    }
   

    

    //echo $a;
    $sql = "SELECT * FROM `lost_table`  where p_id='$id'";//掛失數量
    $result = mysqli_query($conn,$sql) ;//查詢
    $numlost = mysqli_num_rows($result);
    if($numlost > 0){
      while($e=mysqli_fetch_assoc($result)){
        $outputb[]=$e;
      }
      $c = count($outputb);
      //echo $b;
    }else{
      $c = "0";
    }
    

    
    
    $name = $a."/".$b."/".$c."/".$pathname."/".$dogvariety."/".$dogvar2."/".$dogname;
    echo $name;
    mysqli_close($conn);
?>
