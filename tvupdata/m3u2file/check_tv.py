import cv2
import re
from urllib.parse import urlparse

def extract_id_from_url(url):
    """
    从URL路径中提取类似 '3221226471' 的ID作为文件名
    """
    # 1. 解析URL，获取路径部分
    path = urlparse(url).path
    
    # 2. 使用正则表达式提取路径中最后一段纯数字（即 3221226471）
    match = re.search(r'/(\d+)/[^/]*$', path)
    
    if match:
        return match.group(1)
    else:
        # 如果没找到，返回默认名称以防报错
        return "unknown_frame"

def capture_frame_from_stream(url, frame_index=0):
    """
    从视频流中截取指定帧并保存为图片
    """
    # 1. 动态生成文件名
    file_id = extract_id_from_url(url)
    print(f"图片id: {file_id}")
    output_filename = f"tv_jpg/{file_id}.jpg"
    
    print(f"正在连接视频流: {url}")
    
    # 2. 打开视频流
    cap = cv2.VideoCapture(url)
    
    # 检查视频流是否成功打开
    if not cap.isOpened():
        print(f"错误: 无法打开视频流，请检查网络或URL是否正确。")
        return False

    # 3. 跳转到指定的帧
    if frame_index > 0:
        cap.set(cv2.CAP_PROP_POS_FRAMES, frame_index)

    # 4. 读取一帧画面
    ret, frame = cap.read()
    
    # 5. 检查是否成功读取并保存
    if ret:
        cv2.imwrite(output_filename, frame)
        print(f"成功! 第 {frame_index} 帧已保存为 '{output_filename}'")
    else:
        print("错误: 无法从视频流中读取到画面。")

    # 6. 释放视频流资源
    cap.release()
    return ret

def batch_capture_from_file(file_path="ipv6-out.txt", frame_index=0):
    """
    从指定的文本文件中批量读取URL并进行截图
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            # 读取所有行，去除首尾空白字符，并过滤掉空行
            urls = [line.strip() for line in f if line.strip()]
    except FileNotFoundError:
        print(f"错误: 找不到文件 '{file_path}'，请确保文件在当前目录下。")
        return
    except Exception as e:
        print(f"读取文件时发生错误: {e}")
        return

    if not urls:
        print("文件中没有有效的URL。")
        return

    total = len(urls)
    print(f"共读取到 {total} 个URL，开始批量截图...\n{'='*40}")

    success_count = 0
    for i, url in enumerate(urls, 1):
        print(f"[{i}/{total}]")
        if capture_frame_from_stream(url, frame_index):
            success_count += 1

    print(f"{'='*40}\n批量处理完成！成功: {success_count}, 失败: {total - success_count}")

if __name__ == "__main__":
    # 从 ipv6-out.txt 中读取URL并批量截图（默认截取第一帧）
    batch_capture_from_file("ipv6-out.txt", frame_index=0)