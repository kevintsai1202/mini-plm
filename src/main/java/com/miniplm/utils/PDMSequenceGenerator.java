package com.miniplm.utils;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PDMSequenceGenerator extends SequenceStyleGenerator {
  public static final String STRATEGY = "com.chicony.pdm.persistence.PDMSequenceGenerator";

  public static final String PARAM_PREFIX = "prefix";
  public static final String PARAM_DATETIME_FORMAT = "datetimeFormat";
  public static final String PARAM_SEQ_LENGTH = "seqLength";
  public static final String PARAM_PADDING_CHAR = "paddingChar";

  public static final String DATETIME_FORMAT_YYYYMMDD = "yyyyMMdd";

  private String prefix;
  private String datetimeFormat;
  private int seqLength;
  private char paddingChar;

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
    super.configure(LongType.INSTANCE, params, serviceRegistry);

    prefix = ConfigurationHelper.getString(PARAM_PREFIX, params, "");

    SimpleDateFormat sdf =
        new SimpleDateFormat(
            ConfigurationHelper.getString(PARAM_DATETIME_FORMAT, params, DATETIME_FORMAT_YYYYMMDD));
    datetimeFormat = sdf.format(new Date());

    seqLength = Integer.parseInt(ConfigurationHelper.getString(PARAM_SEQ_LENGTH, params, "0"));
    paddingChar = ConfigurationHelper.getString(PARAM_PADDING_CHAR, params, "").charAt(0);
  }

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) {
    long seq = (Long) super.generate(session, object);

    String seqStr = String.valueOf(seq);
    String paddingSeq = StringUtils.leftPad(seqStr, seqLength, paddingChar);

    return String.format("%s%s%s", prefix, datetimeFormat, paddingSeq);
  }
}
