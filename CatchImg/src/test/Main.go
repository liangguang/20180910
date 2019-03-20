package main

import (
	"fmt"
	"runtime"
	"github.com/gammazero/workerpool"
)

const {
	
	POOL_MAXSIZE  = 16         // goroutine 池容量
	PICS_EXT      = ".jpg"     // 图片后缀
	PICS_DIR      = "Enterdesk"     // 存放图片文件夹
	RETRY_TIMES   = 3
}

// 初始化操作
func init() {
	createDir(PICS_DIR)
	runtime.GOMAXPROCS(runtime.NumCPU())
}

// 如果文件夹不存在，则创建文件夹
func createDir(path string) {
	_, err := os.Stat(path)
	if err != nil {
		if !os.IsExist(err) {
			os.Mkdir(path, os.ModePerm)
			fmt.Println("Create pics dir.")
		}
	}
}

func main(){
	//fmt.Println("用GO来爬图片试试！！！")
		// 创建 goroutine 池
	wp := workerpool.New(POOL_MAXSIZE)
	start := time.Now()
	for (a := 1; a<146; a++) {
		url := "https://mm.enterdesk.com/"+a+".html"
		
		wp.Submit(func() {
			downloadPics(url)
		})
	}
	// 等待所有任务完成
	wp.StopWait()
	elapsed := time.Since(start)
	fmt.Println("Elapsed: ", elapsed)
	
}