1、main.py  生成全量的1万个视频地址，只需要执行一次
2、check_urls.py 检测视频地址是否有效，将在线的视频保存到urls-200.txt文件中
3、main-a.py 从urls-200.txt文件中提取所有在线的频道，并根据bj-all.txt中的频道名称，查出ipv6-out.txt（不存在的新频道）和ipv6-have.txt（现在还存在的频道）
4、check_tv.py 从ipv6-out.txt文件中提前视频地址，并打开视频流，并截取视频流第0帧保存成图片，图片存放到tv_jpg目录下，文件名是频道id