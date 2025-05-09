#!/bin/bash

# 定义接口地址
url="http://localhost:8080/v1/detail/list"
# 定义每页大小
pageSize=100
# 定义总记录数
total=10240
# 计算总页数
total_pages=$(( (total + pageSize - 1) / pageSize ))
# 定义保存 tid 的文件
output_file="tid_list.csv"
echo "tid" >> "$output_file"

# 循环请求每一页
for (( page=1; page<=total_pages; page++ )); do
    # 发起 curl 请求
    response=$(curl -s "$url?pageNum=$page&pageSize=$pageSize")
    # 检查请求是否成功
    if [ $? -eq 0 ]; then
        # 使用 jq 提取 tid 并追加到文件
        echo "$response" | jq -r '.data.list[].tid' >> "$output_file"
    else
        echo "请求第 $page 页失败"
    fi
done

echo "所有 tid 已保存到 $output_file"

