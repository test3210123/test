# 生成从 "0000" 到 "9999" 的所有四位数字字符串，
# 每个前面拼接 "HTTP://"，并保存到文件中，每行一个
with open("urls.txt", "w") as file:
    for i in range(10000):
        code = f"{i:04d}"
        file.write(f"http://[2409:8087:8:21::18]:6610/otttv.bj.chinamobile.com/PLTV/88888888/224/322122{code}/1.m3u8?\n")