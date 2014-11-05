package com.nulp.eduframework.util;

public class Constants {
	
	public static final String SECURE_TOKEN_HEADER_NAME = "eduSecureToken";
	
	public static enum ConnectionType {
		
		LECTURE {
			public String getName() {
				return "chat";
			}
		},
		
		PRESENTATION {
			public String getName() {
				return "presentation";
			}
		};
		
		public abstract String getName(); 
	};
	
	public static enum PresentationDirection {
		
		NEXT {
			public String getName() {
				return "next";
			}
		},
		
		PREV {
			public String getName() {
				return "prev";
			}
		},
		
		RESTART {
			public String getName() {
				return "restart";
			}
		};
		
		public abstract String getName(); 
	};

}
