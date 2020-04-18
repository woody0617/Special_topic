<?php
    set_time_limit(0); 
    $testpath = 'C:/xampp/htdocs/fun_img/';
    $file=scandir($testpath); //抓fun_img檔名 接在bat後面
	system("cmd/c C:\\xampp\\htdocs\\docker\\first_dog_or_cat\\test1.bat " .$file[2]);
    //system("cmd/c C:\\xampp\\htdocs\\docker\\first_dog_or_cat\\test1.bat test1.jpg" );
    $array_dogname = file("C:/xampp/htdocs/a.txt");//讀進辨識的結果
    $dogname=$array_dogname[0];//第一行
    $z = "www";
    $x = "nolost";
    if ($dogname =="It is not a dog"){
        echo trim("/".$z."/");
        //刪掉圖片從fun_img抓檔名，把辨識完的圖片砍掉
        $delpath = 'C:/xampp/htdocs/fun_img/';
        $file=scandir($delpath);
        //print_r($file[2]);
        unlink('C:/xampp/htdocs/a.txt');
        unlink('C:/xampp/htdocs/fun_img/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/first_dog_or_cat/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/second_dog_species/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/corgi/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/husky/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/shibaInu/tf_files/'.$file[2]);
            
    }
    elseif($dogname =="lose nothing"){   //如果lose下面沒有檔案
        echo trim("/".$x."/");; //看你要打甚麼
        $delpath = 'C:/xampp/htdocs/fun_img/';
        $file=scandir($delpath);
        //print_r($file[2]);
        unlink('C:/xampp/htdocs/a.txt');
        unlink('C:/xampp/htdocs/fun_img/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/first_dog_or_cat/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/second_dog_species/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/corgi/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/husky/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/shibaInu/tf_files/'.$file[2]);
    }else{
        $array_dogname = file("C:/xampp/htdocs/a.txt");//讀進辨識的結果
        $dogname=$array_dogname[0];//第一行
        $dogname0=trim($dogname);//種類
        $dogname1=$array_dogname[1];//第二行
        $dogname2=trim($dogname1);//資料夾名稱 ex:D14_20
        $dir='C:/xampp/htdocs/docker/third_who_dog/'. $dogname0 .'/tf_files/lose/'.$dogname2;
        $j =6;
        $a = "";
        $b = "";
        $c = "";
        for ($i=0; $i <$j; $i++) {
            $file=scandir($dir);
            $b=rand(3,10);
            $c = $c."/".$file[$b];

        }   

        $a = "/".$dogname0."/".$dogname2.$c;
        echo $a;
        //刪掉圖片從fun_img抓檔名，把辨識完的圖片砍掉
        $delpath = 'C:/xampp/htdocs/fun_img/';
        $file=scandir($delpath);
        //print_r($file[2]);
        unlink('C:/xampp/htdocs/a.txt');
        unlink('C:/xampp/htdocs/fun_img/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/first_dog_or_cat/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/second_dog_species/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/corgi/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/husky/tf_files/'.$file[2]);
        unlink('C:/xampp/htdocs/docker/third_who_dog/shibaInu/tf_files/'.$file[2]);
    //echo "hello";
    }
?>