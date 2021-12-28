package dto;

import java.util.List;

import com.monkata.lps.entity.KenoConfig;
import com.monkata.lps.entity.UserEntity;

import lombok.Data;

@Data
public class InitGameReq {
	public UserEntity user;
}
