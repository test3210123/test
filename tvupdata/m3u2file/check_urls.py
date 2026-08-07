import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
# from urllib.parse import urlparse, urlencode, parse_qsl, urlunparse

def check_url(url):
    """
    检查单个URL的状态码
    返回: (原始url, 状态码, 是否成功)
    """
    try:
        # 设置超时时间，防止某个URL卡住整个脚本
        # print(f"[提示] {url}")
        # fixed_url = urlencode(url)
        # fixed_url = url.replace('[', '%5B').replace(']', '%5D')
        fixed_url = url + "t=1"
        # print(f"[提示] {fixed_url}")
        response = requests.get(fixed_url, timeout=10, allow_redirects=True)
        return (url, response.status_code, response.status_code == 200)
    except requests.exceptions.RequestException as e:
        # 捕获所有网络相关异常（超时、连接拒绝、DNS错误等）
        return (url, f"Error: {e}", False)

def main():
    input_file = "urls.txt"
    success_file = "urls-200.txt"
    error_file = "urls-error.txt"
    
    success_urls = []
    error_urls = []

    # 1. 读取 urls.txt
    try:
        with open(input_file, "r", encoding="utf-8") as f:
            urls = [line.strip() for line in f if line.strip()]
    except FileNotFoundError:
        print(f"[错误] 找不到文件: {input_file}，请先创建该文件并填入URL。")
        return

    if not urls:
        print("[提示] urls.txt 为空，无需执行。")
        return

    # urls= ['http://[2409:8087:8:21::18]:6610/otttv.bj.chinamobile.com/PLTV/88888888/224/3221226895/1.m3u8?t=1']
    print(f"[信息] 共读取到 {len(urls)} 个URL，开始检查...")
	

    # 2. 并发访问URL（提高速度）
    # max_workers 可根据网络情况和电脑配置调整，建议 10-50
    with ThreadPoolExecutor(max_workers=20) as executor:
        future_to_url = {executor.submit(check_url, url): url for url in urls}
        
        for future in as_completed(future_to_url):
            url, status, is_ok = future.result()
            status_str = str(status)
            
            if is_ok:
                success_urls.append(url)
                print(f"[200 OK] {url}")
            else:
                # 错误记录格式: URL | 状态码/错误信息
                error_urls.append(f"{url} | {status_str}")
                print(f"[FAIL]   {url} -> {status_str} {status} {is_ok}")

    # 3. 保存到文件
    with open(success_file, "w", encoding="utf-8") as f:
        f.write("\n".join(success_urls))
        
    with open(error_file, "w", encoding="utf-8") as f:
        f.write("\n".join(error_urls))

    print("\n[完成] 检查结束！")
    print(f"  - 成功 (200): {len(success_urls)} 个 -> {success_file}")
    print(f"  - 失败/其他: {len(error_urls)} 个 -> {error_file}")

if __name__ == "__main__":
    main()