package dto;

import java.util.List;

import lombok.Data;

@Data
public class KenoReq {
	
	public double bet;
	private int total_num;
	public List<Integer> lots;
	public List<Boolean> _aNumSelected;
	public KenoReq(){}
}
