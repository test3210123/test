import re
import uuid
import os
from aip import AipOcr
import base64
import requests

# ================= 配置区域 =================
APP_ID = '124109681' 
API_KEY = '8DSptlM1XDP4npktv1k8l303'
SECRET_KEY = 'Av29lHAnX4pjMpKmutMZd0ABbCBfdly1'
# ===========================================

def get_access_token(api_key, secret_key):
    """获取API访问令牌"""
    host = f'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={api_key}&client_secret={secret_key}'
    response = requests.get(host)
    if response:
        return response.json().get('access_token')
    return None

def recognize_tv_channel(image_path):
    """识别图片中的电视频道"""
    token = get_access_token(API_KEY, SECRET_KEY)
    if not token:
        return "获取Token失败，请检查Key是否正确"

    try:
        with open(image_path, 'rb') as f:
            image_data = f.read()
        image_base64 = base64.b64encode(image_data).decode('utf-8')
    except FileNotFoundError:
        return f"错误：找不到文件 {image_path}"
    except Exception as e:
        return f"读取图片失败：{str(e)}"

    url = f'https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token={token}'
    payload = {'image': image_base64}
    headers = {'Content-Type': 'application/x-www-form-urlencoded'}
    response = requests.post(url, headers=headers, data=payload)

    try:
        result = response.json()
    except Exception:
        return f"请求失败，返回内容不是JSON: {response.text}"

    if 'words_result' not in result:
        return "识别失败：" + str(result)

    all_text = [item['words'] for item in result['words_result']]
    print(f"  -> 识别到的文字有：{all_text}")

    # 定义所有卫视频道的关键词映射表
    channel_map = {
        'CCTV': 'CCTV', 'cctv': 'CCTV', '央视': 'CCTV',
        '河南': '河南卫视', '湖南': '湖南卫视', '东方': '东方卫视',
        '江苏': '江苏卫视', '浙江': '浙江卫视', '北京卫视': '北京卫视',
        '广东': '广东卫视', '深圳': '深圳卫视', '山东': '山东卫视',
        '安徽': '安徽卫视', '四川': '四川卫视', '湖北': '湖北卫视',
        '辽宁': '辽宁卫视', '黑龙江': '黑龙江卫视', '吉林': '吉林卫视',
        '天津': '天津卫视', '重庆': '重庆卫视', '河北': '河北卫视',
        '山西': '山西卫视', '陕西': '陕西卫视', '甘肃': '甘肃卫视',
        '宁夏': '宁夏卫视', '青海': '青海卫视', '新疆': '新疆卫视',
        '西藏': '西藏卫视', '内蒙古': '内蒙古卫视', '广西': '广西卫视',
        '云南': '云南卫视', '贵州': '贵州卫视', '海南': '海南卫视',
        '福建': '福建卫视', '江西': '江西卫视', '河南国际': '河南国际频道',
    }

    # 优先匹配具体频道
    for text in all_text:
        for keyword, channel_name in channel_map.items():
            if keyword in text:
                # 特殊处理CCTV，尝试提取数字
                if 'CCTV' in keyword or '央视' in keyword:
                    match = re.search(r'CCTV[-\s]*(\d+)', text, re.IGNORECASE)
                    if match:
                        return f"{channel_name}-{match.group(1)}"
                    else:
                        random_suffix = str(uuid.uuid4())[:8]
                        return f"{channel_name}-未知-{random_suffix}"
                else:
                    return f"{channel_name}"

    # 未匹配到任何已知频道，取 words_result 中的前2个词作为结果
    if all_text:
        fallback_text = "-".join(all_text[:2])
        return f"未匹配频道({fallback_text})"
    else:
        return "未识别到任何文字"

# 【新增功能】批量处理函数
def batch_recognize(folder_path):
    """遍历指定文件夹下的所有 .jpg 图片并进行识别"""
    if not os.path.exists(folder_path):
        print(f"错误：文件夹 '{folder_path}' 不存在")
        return

    # 获取目录下所有以 .jpg 结尾的文件
    jpg_files = [f for f in os.listdir(folder_path) if f.lower().endswith('.jpg')]
    
    if not jpg_files:
        print(f"文件夹 '{folder_path}' 下没有找到 .jpg 图片")
        return

    print(f"共找到 {len(jpg_files)} 张图片，开始识别...\n")
    
    # 遍历并处理每一张图片
    for filename in jpg_files:
        img_path = os.path.join(folder_path, filename)
        result = recognize_tv_channel(img_path)
        # 格式化打印文件名和识别结果
        print(f"[{filename}] 识别结果：{result}\n")

# 使用示例
if __name__ == '__main__':
    target_folder = 'tv_jpg'
    batch_recognize(target_folder)