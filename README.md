## 首页


### 1、藏品
#### url 
get - /api/index
#### 入参

| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| type    |   int |  1：创造权；2：使用权  |
| sort    |   int |  1：价钱正序；2：价钱倒序  |
| max_id    |   int |  0：最新；分页传maxId ; -1 无数据 |

#### 出参

```
{
	time:10000
	code:200
	data:{
		branners:[{},{}],
		list:[{},{}],
		maxId:0
	}
}
```

---- 

###  2、登入、注册
#### url 
get - /api/login
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| code        |   str |  code  |
| avatarUrl   |   str |  不为空可以给空串 avatarUrl= |
| nickName    |   str |  ...  |
| gender      |   str |  ...  |

#### 出参

```
{
	"code": 200,
	"message": "login",
	"data": {
		"income": "1205",
		"openid": "ov8p35OEtLxO7nILiHq6rmBCpkv4",
		"session_key": "LSH3Ho44x3G53ZZIEJewDQ==",
		"userid": 1
	},
	"success": true
}
```

---

### 3、查询用户信息
#### url 
get - /api/get_user
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  openId  |

#### 出参

```

{
	"code": 200,
	"message": "get_user",
	"data": {
		"id": 1,
		"open_id": "ov8p35OEtLxO7nILiHq6rmBCpkv4",
		"nick_name": "weqeq",
		"avatar_url": "123/132",
		"gender": "1",
		"income": "2134",
		"create_time": "2019-07-22 14:24:13.0",
		"modify_time": "2019-07-22 14:24:13.0"
	},
	"success": true
}

```

---

### 4、提现 - 企业付款 
#### url 
get - /api/withdrawal
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  openId  |
| money    |   int |  金额  |
| 安全内容    |   int |  安全内容  |


#### 出参

```
{
	time:10000
	code:200
	data: true
}
```

---

### 5、订单 
#### url 
get - /api/order
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| type    |   int |  1、未支付；2、已完成  |
| open_id    |   str |  用户id  |
| max_id    |   int |  0：最新；分页传maxId  |


#### 出参

```
{
	time:10000
	code:200
	data: {
		order:[{},{}]
	}
}
```
--- 

### 6、上传藏品 - 基本信息
#### url 
post - /api/upload_image  全都是 form_data
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  open_id  |
| name    |   str |  姓名  |
| tel_no    |   str |  手机号 |
| draw_name    |   str |  作品名称  |
| author    |   str |  作者  |
| desc    |   str |  简介  |
| draw_file    |   file |  form_data   |

#### 出参

```
{
	"code": 200,
	"message": "upload_image",
	"data": true,
	"success": true
}
```

--- 

### 7、图片详情 - check
#### url 
get - /api/image_info  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| open_id    |   str |  openID  |


#### 出参

```
{
	time:10000
	code:200
	data:{
		drawInfo:{
		},
		isFavorite:true,
		isHavePush:true
	}
}
```

---

### 8、报表
#### url 
get - /api/statistics  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| range    |   str |  1、日7天 ；2、月；3、历史以来  |

#### 出参

```
{
	time:10000
	code:200
	data:{
		create_income_list:[],
		create_prize_list:[],
		owner_income_list[],
		owner_prize_list[],

	}
}
```
---

### 9、推送到花屏
#### url 
get - /api/push  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```

---



### 10、购买
#### url 
get - /api/buy  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| type    |   str |  1、创作权；2、所有权 |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```


---

### 11、购买  客户端唤起支付，服务端需要
#### url 
get - /api/buy  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| type    |   str |  1、创作权；2、所有权 |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```

---

### 12、下单 后端拼参数调用微信接口。
#### url 
get - /api/buy  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |  图片ID  |
| type    |   str |  1、创作权；2、所有权 |

#### 出参

```
{
	time:10000
	code:200
	data:true
}
```

---

### 13、我的藏品
#### url 
get - /api/my_draw  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| open_id    |   str |  openId  |
| max_id    |   int |  0：最新；分页传maxId  |
| type    |   int |  1、创造权；2、所有权  |


#### 出参

```
{
	time:10000
	code:200
	data:{
		list:[]
	}
}
```

### 14、修改藏品
#### url 
get - /api/update_draw  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_id    |   str |   |
| create_price    |   int |  创造这价格 分 |
| owner_count    |   int |  所有权份额 分 |

#### 出参

```
{
	"code": 200,
	"message": "upload_image",
	"data": true,
	"success": true
}
```

### 15、修改藏品所有权价格
#### url 
get - /api/update_owner_draw  
#### 入参
| 字段      |     类型 |   说明   |
| :-------- | --------:| :------: |
| draw_ext_id    |   str |   |
| owner_prize  |   int |  所有权份额 分 |
| income_prize  |   int |  所有权份额 分 |
| proof  |   file | 凭证 |


#### 出参

```
{
	time:10000
	code:200
	data:true
}
```

### 

- [X]  藏品
- [X]  登入、注册
- [X]  查询用户信息
- [X]  上传藏品 - 基本信息
- [X]  修改藏品






