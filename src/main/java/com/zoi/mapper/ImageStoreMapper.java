package com.zoi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoi.entity.dto.StoreImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageStoreMapper extends BaseMapper<StoreImage> {
}
