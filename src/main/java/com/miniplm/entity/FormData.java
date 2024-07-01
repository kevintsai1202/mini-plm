package com.miniplm.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.miniplm.convert.ConverterListJson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Proxy(lazy = true)
@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString(exclude = {"form"})
@Table(name = "MP_FORM_DATA")
@SQLDelete(sql = "UPDATE MP_FORM_DATA SET enabled=0 WHERE id=?")
@Where(clause = "enabled = true")
@SequenceGenerator(name="MP_SEQUENCE_GENERATOR", sequenceName="MP_SEQ", initialValue=1, allocationSize=1)
@JsonInclude(value=Include.NON_NULL)
public class FormData extends BaseEntity{
	

//    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MP_SEQUENCE_GENERATOR")
    @Column(name = "ID", unique = true, nullable = false)
	@JsonIgnore
	private Long fdId;
    
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(fdId);
        return hcb.toHashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FormData)) {
            return false;
        }
        FormData that = (FormData) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(fdId, that.fdId);
        return eb.isEquals();
    }
	
    @Column(name = "text01", length = 200)
    private String text01;

    @Column(name = "text02", length = 200)
    private String text02;
    
    @Column(name = "text03", length = 200)
    private String text03;
    
    @Column(name = "text04", length = 200)
    private String text04;
    
    @Column(name = "text05", length = 200)
    private String text05;
    
    @Column(name = "text06", length = 200)
    private String text06;
    
    @Column(name = "text07", length = 200)
    private String text07;
    
    @Column(name = "text08", length = 200)
    private String text08;
    
    @Column(name = "text09", length = 200)
    private String text09;
    	
    @Column(name = "text10", length = 200)
    private String text10;
    
    @Column(name = "text11", length = 200)
    private String text11;
    
    @Column(name = "text12", length = 200)
    private String text12;
    
    @Column(name = "text13", length = 200)
    private String text13;
    
    @Column(name = "text14", length = 200)
    private String text14;
    
    @Column(name = "text15", length = 200)
    private String text15;
    
    @Column(name = "text16", length = 200)
    private String text16;
    
    @Column(name = "text17", length = 200)
    private String text17;
    
    @Column(name = "text18", length = 200)
    private String text18;
    
    @Column(name = "text19", length = 200)
    private String text19;
    
    @Column(name = "text20", length = 200)
    private String text20;
    
    @Column(name = "text21", length = 200)
    private String text21;
    
    @Column(name = "text22", length = 200)
    private String text22;
    
    @Column(name = "text23", length = 200)
    private String text23;
    
    @Column(name = "text24", length = 200)
    private String text24;
    
    @Column(name = "text25", length = 200)
    private String text25;
    
    @Column(name = "text26", length = 200)
    private String text26;
    
    @Column(name = "text27", length = 200)
    private String text27;
    
    @Column(name = "text28", length = 200)
    private String text28;
    
    @Column(name = "text29", length = 200)
    private String text29;
    
    @Column(name = "text30", length = 200)
    private String text30;
    
    @Column(name = "textarea01", length = 2000)
    private String textarea01;
    
    @Column(name = "textarea02", length = 2000)
    private String textarea02;
    
    @Column(name = "textarea03", length = 2000)
    private String textarea03;
    
    @Column(name = "textarea04", length = 2000)
    private String textarea04;
    
    @Column(name = "textarea05", length = 2000)
    private String textarea05;
    
    @Column(name = "textarea06", length = 2000)
    private String textarea06;
    
    @Column(name = "textarea07", length = 2000)
    private String textarea07;
    
    @Column(name = "textarea08", length = 2000)
    private String textarea08;
    
    @Column(name = "textarea09", length = 2000)
    private String textarea09;
    
    @Column(name = "textarea10", length = 2000)
    private String textarea10;
    
    @Column(name = "textarea11", length = 2000)
    private String textarea11;
    
    @Column(name = "textarea12", length = 2000)
    private String textarea12;
    
    @Column(name = "textarea13", length = 2000)
    private String textarea13;
    
    @Column(name = "textarea14", length = 2000)
    private String textarea14;
    
    @Column(name = "textarea15", length = 2000)
    private String textarea15;
    
    @Column(name = "textarea16", length = 2000)
    private String textarea16;
    
    @Column(name = "textarea17", length = 2000)
    private String textarea17;
    
    @Column(name = "textarea18", length = 2000)
    private String textarea18;
    
    @Column(name = "textarea19", length = 2000)
    private String textarea19;
    
    @Column(name = "textarea20", length = 2000)
    private String textarea20;
    
    @Column(name = "textarea21", length = 2000)
    private String textarea21;
    
    @Column(name = "textarea22", length = 2000)
    private String textarea22;
    
    @Column(name = "textarea23", length = 2000)
    private String textarea23;
    
    @Column(name = "textarea24", length = 2000)
    private String textarea24;
    
    @Column(name = "textarea25", length = 2000)
    private String textarea25;
    
    @Column(name = "textarea26", length = 2000)
    private String textarea26;
    
    @Column(name = "textarea27", length = 2000)
    private String textarea27;
    
    @Column(name = "textarea28", length = 2000)
    private String textarea28;
    
    @Column(name = "textarea29", length = 2000)
    private String textarea29;
    
    @Column(name = "textarea30", length = 2000)
    private String textarea30;
    
    @Column(name = "date01")
    private Date date01;
    
    @Column(name = "date02")
    private Date date02;

    @Column(name = "date03")
    private Date date03;
    
    @Column(name = "date04")
    private Date date04;
    
    @Column(name = "date05")
    private Date date05;
    
    @Column(name = "date06")
    private Date date06;
    
    @Column(name = "date07")
    private Date date07;
    
    @Column(name = "date08")
    private Date date08;
    
    @Column(name = "date09")
    private Date date09;
    
    @Column(name = "date10")
    private Date date10;
    
    @Column(name = "date11")
    private Date date11;
    
    @Column(name = "date12")
    private Date date12;
    
    @Column(name = "date13")
    private Date date13;
    
    @Column(name = "date14")
    private Date date14;
    
    @Column(name = "date15")
    private Date date15;
    
    @Column(name = "date16")
    private Date date16;
    
    @Column(name = "date17")
    private Date date17;
    
    @Column(name = "date18")
    private Date date18;
    
    @Column(name = "date19")
    private Date date19;
    
    @Column(name = "date20")
    private Date date20;
    
    @Column(name = "date21")
    private Date date21;
    
    @Column(name = "date22")
    private Date date22;
    
    @Column(name = "date23")
    private Date date23;
    
    @Column(name = "date24")
    private Date date24;
    
    @Column(name = "date25")
    private Date date25;
    
    @Column(name = "date26")
    private Date date26;
    
    @Column(name = "date27")
    private Date date27;
    
    @Column(name = "date28")
    private Date date28;
    
    @Column(name = "date29")
    private Date date29;
    
    @Column(name = "date30")
    private Date date30;
    
    @Column(name = "select01", length = 200)
    private String select01;
    
    @Column(name = "select02", length = 200)
    private String select02;
    
    @Column(name = "select03", length = 200)
    private String select03;
    
    @Column(name = "select04", length = 200)
    private String select04;
    
    @Column(name = "select05", length = 200)
    private String select05;
    
    @Column(name = "select06", length = 200)
    private String select06;
    
    @Column(name = "select07", length = 200)
    private String select07;
    
    @Column(name = "select08", length = 200)
    private String select08;
    
    @Column(name = "select09", length = 200)
    private String select09;
    
    @Column(name = "select10", length = 200)
    private String select10;
    
    @Column(name = "select11", length = 200)
    private String select11;
    
    @Column(name = "select12", length = 200)
    private String select12;
    
    @Column(name = "select13", length = 200)
    private String select13;
    
    @Column(name = "select14", length = 200)
    private String select14;
    
    @Column(name = "select15", length = 200)
    private String select15;
    
    @Column(name = "select16", length = 200)
    private String select16;
    
    @Column(name = "select17", length = 200)
    private String select17;
    
    @Column(name = "select18", length = 200)
    private String select18;
    
    @Column(name = "select19", length = 200)
    private String select19;
    
    @Column(name = "select20", length = 200)
    private String select20;
    
    @Column(name = "select21", length = 200)
    private String select21;
    
    @Column(name = "select22", length = 200)
    private String select22;
    
    @Column(name = "select23", length = 200)
    private String select23;
    
    @Column(name = "select24", length = 200)
    private String select24;
    
    @Column(name = "select25", length = 200)
    private String select25;
    
    @Column(name = "select26", length = 200)
    private String select26;
    
    @Column(name = "select27", length = 200)
    private String select27;
    
    @Column(name = "select28", length = 200)
    private String select28;
    
    @Column(name = "select29", length = 200)
    private String select29;
    
    @Column(name = "select30", length = 200)
    private String select30;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist01", length = 2000)
    private LinkedList<String> multilist01;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist02", length = 2000)
    private LinkedList<String> multilist02;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist03", length = 2000)
    private LinkedList<String> multilist03;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist04", length = 2000)
    private LinkedList<String> multilist04;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist05", length = 2000)
    private LinkedList<String> multilist05;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist06", length = 2000)
    private LinkedList<String> multilist06;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist07", length = 2000)
    private LinkedList<String> multilist07;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist08", length = 2000)
    private LinkedList<String> multilist08;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist09", length = 2000)
    private LinkedList<String> multilist09;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist10", length = 2000)
    private LinkedList<String> multilist10;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist11", length = 2000)
    private LinkedList<String> multilist11;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist12", length = 2000)
    private LinkedList<String> multilist12;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist13", length = 2000)
    private LinkedList<String> multilist13;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist14", length = 2000)
    private LinkedList<String> multilist14;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist15", length = 2000)
    private LinkedList<String> multilist15;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist16", length = 2000)
    private LinkedList<String> multilist16;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist17", length = 2000)
    private LinkedList<String> multilist17;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist18", length = 2000)
    private LinkedList<String> multilist18;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist19", length = 2000)
    private LinkedList<String> multilist19;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist20", length = 2000)
    private LinkedList<String> multilist20;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist21", length = 2000)
    private LinkedList<String> multilist21;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist22", length = 2000)
    private LinkedList<String> multilist22;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist23", length = 2000)
    private LinkedList<String> multilist23;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist24", length = 2000)
    private LinkedList<String> multilist24;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist25", length = 2000)
    private LinkedList<String> multilist25;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist26", length = 2000)
    private LinkedList<String> multilist26;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist27", length = 2000)
    private LinkedList<String> multilist27;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist28", length = 2000)
    private LinkedList<String> multilist28;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist29", length = 2000)
    private LinkedList<String> multilist29;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "multilist30", length = 2000)
    private LinkedList<String> multilist30;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox01", length = 2000)
    private LinkedList<String> checkbox01;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox02", length = 2000)
    private LinkedList<String> checkboxt02;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox03", length = 2000)
    private LinkedList<String> checkbox03;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox04", length = 2000)
    private LinkedList<String> checkbox04;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox05", length = 2000)
    private LinkedList<String> checkbox05;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox06", length = 2000)
    private LinkedList<String> checkbox06;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox07", length = 2000)
    private LinkedList<String> checkbox07;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox08", length = 2000)
    private LinkedList<String> checkbox08;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox09", length = 2000)
    private LinkedList<String> checkbox09;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox10", length = 2000)
    private LinkedList<String> checkbox10;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox11", length = 2000)
    private LinkedList<String> checkbox11;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox12", length = 2000)
    private LinkedList<String> checkbox12;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox13", length = 2000)
    private LinkedList<String> checkbox13;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox14", length = 2000)
    private LinkedList<String> checkbox14;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox15", length = 2000)
    private LinkedList<String> checkbox15;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox16", length = 2000)
    private LinkedList<String> checkbox16;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox17", length = 2000)
    private LinkedList<String> checkbox17;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox18", length = 2000)
    private LinkedList<String> checkbox18;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox19", length = 2000)
    private LinkedList<String> checkbox19;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox20", length = 2000)
    private LinkedList<String> checkbox20;

    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox21", length = 2000)
    private LinkedList<String> checkbox21;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox22", length = 2000)
    private LinkedList<String> checkbox22;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox23", length = 2000)
    private LinkedList<String> checkbox23;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox24", length = 2000)
    private LinkedList<String> checkbox24;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox25", length = 2000)
    private LinkedList<String> checkbox25;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox26", length = 2000)
    private LinkedList<String> checkbox26;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox27", length = 2000)
    private LinkedList<String> checkbox27;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox28", length = 2000)
    private LinkedList<String> checkbox28;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox29", length = 2000)
    private LinkedList<String> checkbox29;
    
    @Convert(converter = ConverterListJson.class)
    @Column(name = "checkbox30", length = 2000)
    private LinkedList<String> checkbox30;
    
    @Column(name = "radio01", length = 200)
    private String radio01;
    
    @Column(name = "radio02", length = 200)
    private String radio02;
    
    @Column(name = "radio03", length = 200)
    private String radio03;
    
    @Column(name = "radio04", length = 200)
    private String radio04;
    
    @Column(name = "radio05", length = 200)
    private String radio05;
    
    @Column(name = "radio06", length = 200)
    private String radio06;
    
    @Column(name = "radio07", length = 200)
    private String radio07;
    
    @Column(name = "radio08", length = 200)
    private String radio08;
    
    @Column(name = "radio09", length = 200)
    private String radio09;
    
    @Column(name = "radio10", length = 200)
    private String radio10;

    @Column(name = "radio11", length = 200)
    private String radio11;
    
    @Column(name = "radio12", length = 200)
    private String radio12;
    
    @Column(name = "radio13", length = 200)
    private String radio13;
    
    @Column(name = "radio14", length = 200)
    private String radio14;
    
    @Column(name = "radio15", length = 200)
    private String radio15;
    
    @Column(name = "radio16", length = 200)
    private String radio16;
    
    @Column(name = "radio17", length = 200)
    private String radio17;
    
    @Column(name = "radio18", length = 200)
    private String radio18;
    
    @Column(name = "radio19", length = 200)
    private String radio19;
    
    @Column(name = "radio20", length = 200)
    private String radio20;
    
    @Column(name = "radio21", length = 200)
    private String radio21;
    
    @Column(name = "radio22", length = 200)
    private String radio22;
    
    @Column(name = "radio23", length = 200)
    private String radio23;
    
    @Column(name = "radio24", length = 200)
    private String radio24;
    
    @Column(name = "radio25", length = 200)
    private String radio25;
    
    @Column(name = "radio26", length = 200)
    private String radio26;
    
    @Column(name = "radio27", length = 200)
    private String radio27;
    
    @Column(name = "radio28", length = 200)
    private String radio28;
    
    @Column(name = "radio29", length = 200)
    private String radio29;
    
    @Column(name = "radio30", length = 200)
    private String radio30;
    
    @Column(name = "number01")
    private Double number01;
    
    @Column(name = "number02")
    private Double number02;
    
    @Column(name = "number03")
    private Double number03;
    
    @Column(name = "number04")
    private Double number04;
    
    @Column(name = "number05")
    private Double number05;
    
    @Column(name = "number06")
    private Double number06;
    
    @Column(name = "number07")
    private Double number07;
    
    @Column(name = "number08")
    private Double number08;
    
    @Column(name = "number09")
    private Double number09;
    
    @Column(name = "number10")
    private Double number10;
    
    @Column(name = "number11")
    private Double number11;
    
    @Column(name = "number12")
    private Double number12;
    
    @Column(name = "number13")
    private Double number13;
    
    @Column(name = "number14")
    private Double number14;
    
    @Column(name = "number15")
    private Double number15;
    
    @Column(name = "number16")
    private Double number16;
    
    @Column(name = "number17")
    private Double number17;
    
    @Column(name = "number18")
    private Double number18;
    
    @Column(name = "number19")
    private Double number19;
    
    @Column(name = "number20")
    private Double number20;
    
    @Column(name = "number21")
    private Double number21;
    
    @Column(name = "number22")
    private Double number22;
    
    @Column(name = "number23")
    private Double number23;
    
    @Column(name = "number24")
    private Double number24;
    
    @Column(name = "number25")
    private Double number25;
    
    @Column(name = "number26")
    private Double number26;
    
    @Column(name = "number27")
    private Double number27;
    
    @Column(name = "number28")
    private Double number28;
    
    @Column(name = "number29")
    private Double number29;
    
    @Column(name = "number30")
    private Double number30;
    
    @Column(name = "image01", length = 200)
    private String image01;
    
    @Column(name = "image02", length = 200)
    private String image02;
    
    @Column(name = "image03", length = 200)
    private String image03;
    
    @Column(name = "image04", length = 200)
    private String image04;
    
    @Column(name = "image05", length = 200)
    private String image05;
    
    @Column(name = "upload01", length = 2000)
    private String upload01;
    
    @Column(name = "upload02", length = 2000)
    private String upload02;
    
    @Column(name = "upload03", length = 2000)
    private String upload03;
    
    @Column(name = "upload04", length = 2000)
    private String upload04;
    
    @Column(name = "upload05", length = 2000)
    private String upload05;
    
    @Column(name = "html01", length = 2000)
    private String html01;
    
    @Column(name = "html02", length = 2000)
    private String html02;
    
    @Column(name = "html03", length = 2000)
    private String html03;
    
    @Column(name = "html04", length = 2000)
    private String html04;
    
    @Column(name = "html05", length = 2000)
    private String html05;
    
    @Column(name = "html06", length = 2000)
    private String html06;
    
    @Column(name = "html07", length = 2000)
    private String html07;
    
    @Column(name = "html08", length = 2000)
    private String html08;
    
    @Column(name = "html09", length = 2000)
    private String html09;
    
    @Column(name = "html10", length = 2000)
    private String html10;
    
    @JsonIgnore
    @OneToOne(mappedBy = "formData", fetch = FetchType.EAGER)
    @JoinColumn(name = "FORM_ID", referencedColumnName = "ID")
    private Form form;
}
