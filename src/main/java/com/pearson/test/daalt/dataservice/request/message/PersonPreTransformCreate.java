package com.pearson.test.daalt.dataservice.request.message;

import java.util.ArrayList;
import java.util.List;


import com.pearson.test.daalt.dataservice.model.User;

public class PersonPreTransformCreate extends SubPubTransformerMessage {
	
	public PersonPreTransformCreate () {
		payload = new PersonPreTransformMsgPayload();
		super.SetSubscriptionString("Idm.IdentityProfile.Created");
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue)
			throws UnknownPropertyException, InvalidStateException {
		PersonPreTransformMsgPayload specificPayload = (PersonPreTransformMsgPayload) payload;
		switch(propertyName) {
			case "Person" :
				User user = (User) propertyValue;
				specificPayload.entity.givenName = user.getGivenName();
				specificPayload.entity.familyName = user.getFamilyName();
				specificPayload.entityRef.uri = "https://int-piapi.stg-openclass.com/v1/piapi-int/identityprofiles/" + user.getPersonId();
				specificPayload.entityRef.id = user.getPersonId();
				
				specificPayload.entity.identity.id = user.getPersonId();
				specificPayload.entity.identity.uri = "https://int-piapi.stg-openclass.com/v1/piapi-int/identities/" + user.getPersonId();
				
				PersonPreTransformMsgPayload.Entity.Email email1 = specificPayload.entity.new Email();
				email1.id = "email_id_01" + user.getPersonId();
				email1.emailAddress = "email01" + user.getPersonId() +"@mailinator.com";
				email1.isPrimary = true;
				specificPayload.entity.emails.add(email1);
				PersonPreTransformMsgPayload.Entity.Email email2 = specificPayload.entity.new Email();
				email2.id = "email_id_02" + user.getPersonId();
				email2.emailAddress = "email02" + user.getPersonId() +"@mailinator.com";
				email2.isPrimary = false;
				specificPayload.entity.emails.add(email2);
				
				break;
			default : 
				throw new UnknownPropertyException("Failed to build message due to unknown property : "
						+ propertyName);
		}
		
	}

	@Override
	public String getMissingCriticalPropertyName() {
		PersonPreTransformMsgPayload specificPayload = (PersonPreTransformMsgPayload) payload;
		if(specificPayload.entity.givenName == null){
			return "PersonPreTransformMsgPayload.givenName";
		}
		if(specificPayload.entity.familyName == null){
			return "PersonPreTransformMsgPayload.familyName";
		}
		if(specificPayload.entity.emails == null){
			return "PersonPreTransformMsgPayload.email";
		}
		if(specificPayload.entity.identity.id == null){
			return "PersonPreTransformMsgPayload.identity.id";
		}
		return null;
	}
	
	
	public class PersonPreTransformMsgPayload extends TransformerMessagePayload{
		
		public String eventAt = getPastOrFutureTimeFormatted(0);
		public String action = "http://idm.api.pearson.com/events/action#CREATE";
		public EntityRef entityRef = new EntityRef();
		public class EntityRef {
			public String uri;
			public String id;
		}
		public Entity entity = new Entity();
		public class Entity {
			public Identity identity = new Identity();
			public class  Identity {
				public String uri;
				public String id;
			}
			public String createdAt = getPastOrFutureTimeFormatted(-1);
			public String updatedAt = getPastOrFutureTimeFormatted(0);
			public String givenName;
			public String middleName = "MiddleNameOptional";
			public String familyName;
			public String suffix = "Suffix_Optional";
			public List<Email> emails = new ArrayList<Email>();
			public class Email {
				public String id;
				public String emailAddress;
				public boolean isPrimary;
			}
			
		}
		
	}

	
}


/*
{
    "eventAt": "2015-07-22T15:10:12+0000",
    "action": "http://idm.api.pearson.com/events/action#CREATE",
    "entityRef": {
        "uri": "https://int-piapi.stg-openclass.com/v1/piapi-int/identityprofiles/ffffffff55afb254e4b089c1791cd158",
        "id": "ffffffff55afb254e4b089c1791cd158"
    },
    "entity": {
        "identity": {
            "uri": "https://int-piapi.stg-openclass.com/v1/piapi-int/identities/ffffffff55afb254e4b089c1791cd158",
            "id": "ffffffff55afb254e4b089c1791cd158"
        },
        "createdAt": "2015-07-22T15:10:12+0000",
        "updatedAt": "2015-07-22T15:10:12+0000",
        "givenName": "QTST-Raven-reg-upr-fname",
        "familyName": "QTST-Raven-reg-upr-lname",
        "emails": [{
                "id": "00002680d08540cdba2b880f2a16c6c0",
                "emailAddress": "qtst-raven-reg-upr-06466@mailinator.com",
                "isPrimary": "true"
            }
        ]
    }
}

*/