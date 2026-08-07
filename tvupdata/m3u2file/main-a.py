def extract_numbers_from_ipv6_file(filename):
    """从 ipv6-a.txt 提取所有目标数字"""
    numbers = set()  # 使用 set 避免重复
    with open(filename, 'r', encoding='utf-8') as file:
        for line in file:
            line = line.strip()
            if not line:
                continue
            parts = line.split('/')
            try:
                num_str = parts[-2]
                if num_str.isdigit():
                    numbers.add(num_str)
                # else: 可选忽略警告
            except IndexError:
                pass  # 忽略格式错误行（或可打印警告）
    return numbers

def search_and_output_bj_lines(numbers, bj_filename):
    """
    在 bj-all.txt 中查找包含任一 num 的行，每个 num 只输出第一次匹配的行的逗号前部分。
    返回已成功匹配的 num 集合。
    """
    matched_nums = set()
    print_url = []
    already_output = set()  # 记录已经输出过的 num，确保只输出一次
    try:
        with open(bj_filename, 'r', encoding='utf-8') as file:
            for line in file:
                line = line.strip()
                if not line:
                    continue
                # 检查该行是否包含任意一个尚未输出的目标数字
                for num in numbers:
                    if num in already_output:
                        continue  # 已输出过，跳过
                    if num in line:
                        comma_index = line.find(',')
                        if comma_index != -1:
                            prefix = line[:comma_index]
                            print(f"{prefix},http://[2409:8087:8:21::18]:6610/otttv.bj.chinamobile.com/PLTV/88888888/224/{num}/1.m3u8?")
                            print_url.append(f"{prefix},http://[2409:8087:8:21::18]:6610/otttv.bj.chinamobile.com/PLTV/88888888/224/{num}/1.m3u8?")
                        else:
                            print(line)
                        matched_nums.add(num)
                        already_output.add(num)  # 标记为已输出
                        break  # 同一行不处理其他 num（保持你原逻辑）
        # 保存到文件
        with open("ipv6-have.txt", "w", encoding="utf-8") as f:
            f.write("\n".join(print_url))
    except FileNotFoundError:
        print(f"错误：文件 '{bj_filename}' 未找到。")
    return matched_nums

# 主程序
if __name__ == "__main__":
    try:
        nums = extract_numbers_from_ipv6_file("urls-200.txt")
        if not nums:
            print("未从 urls-200.txt 中提取到任何有效数字。")
        else:
            matched = search_and_output_bj_lines(nums, "bj-all.txt")
            # 输出未匹配的 num
            unmatched = nums - matched
            print_url = []
            for num in sorted(unmatched):  # sorted 保证顺序稳定（可选）
                print(f"http://[2409:8087:8:21::18]:6610/otttv.bj.chinamobile.com/PLTV/88888888/224/{num}/1.m3u8?")
                print_url.append(f"http://[2409:8087:8:21::18]:6610/otttv.bj.chinamobile.com/PLTV/88888888/224/{num}/1.m3u8?")
            # 保存到文件
            with open("ipv6-out.txt", "w", encoding="utf-8") as f:
                f.write("\n".join(print_url))
    except FileNotFoundError:
        print("错误：文件 'ipv6-a.txt' 未找到。")