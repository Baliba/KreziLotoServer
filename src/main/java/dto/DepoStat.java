package dto;

import lombok.Data;

@Data
public class DepoStat {
	
	// depot total
	double depo_tt;
	// depot moncash
	double depo_mc;
	// depot credit card
	double depo_cc;
	// depot seller 
	double depo_slr;
	
    public DepoStat() {
    	
    }
}
