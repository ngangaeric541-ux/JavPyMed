DEVICE_PROFILES = {
    "QVGA 320x240": {
        "container": "avi",
        "video_codec": "mpeg4",
        "width": 320,
        "height": 240,
        "framerate": 23,
        "audio_codec": "libmp3lame", 
        "audio_bitrate_kbps": 96,
        "audio_channels": 2,          
    },
    "iPod Video": {
        "container": "mp4",
        "video_codec": "libx264",
        "width": 320,
        "height": 240,
        "framerate": 30,
        "video_bitrate_kbps": 768,
        "h264_profile": "baseline",
        "h264_level": "1.3",
        "audio_codec": "aac",
        "audio_bitrate_kbps": 160,
        "audio_channels": 2,
    },
    "iPod Classic": {
        "container": "mp4",
        "video_codec": "libx264",
        "width": 320,
        "height": 240,
        "framerate": 30,
        "video_bitrate_kbps": 2500,
        "h264_profile": "baseline",
        "h264_level": "3.0",
        "audio_codec": "aac",
        "audio_bitrate_kbps": 160,
        "audio_channels": 2,
    },
    "Early Android Phone": {
        "container": "mp4",
        "video_codec": "libx264",
        "width": 480,
        "height": 320,
        "framerate": 30,
        "video_bitrate_kbps": 1500,
        "h264_profile": "baseline",
        "h264_level": "3.1",
        "audio_codec": "aac",
        "audio_bitrate_kbps": 128,
        "audio_channels": 2,
    },
    "Modern smartphone (1080p)": {
        "container": "mp4",
        "video_codec": "libx264",
        "width": 1920,
        "height": 1080,
        "framerate": 30,
        "video_bitrate_kbps": 8000,
        "h264_profile": "high",
        "h264_level": "4.1",
        "audio_codec": "aac",
        "audio_bitrate_kbps": 192,
        "audio_channels": 2,
    },
}


def get_profile(name):

    if name not in DEVICE_PROFILES:
        available = ", ".join(DEVICE_PROFILES.keys())
        raise ValueError(f"Unknown profile '{name}'. Available: {available}")
    return DEVICE_PROFILES[name]