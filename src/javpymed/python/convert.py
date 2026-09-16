
import argparse
import os
import platform
import re
import subprocess
import sys
 
from profiles import get_profile

def find_ffmpeg():
    here = os.path.dirname(os.path.abspath(__file__))
    system = platform.system()
    #check for os version and type
    if system == "Windows":
        relative_path = os.path.join("ffmpeg-bin","windows","ffmpeg.exe")
        ffmpeg_path = "src/javpymed/ffmpeg-bin/windows/ffmpeg-n9.0.1-29-gad500d59cb-win64-lgpl-shared-9.0/bin/ffmpeg.exe"
    elif system == "Linux":
        relative_path = os.path.join("ffmpeg-bin","linux","ffmpeg")
        ffmpeg_path = "src/javpymed/ffmpeg-bin/linux/ffmpeg"
    elif system == "Darwin":
        relative_path = os.path.join("ffmpeg-bin","mac","ffmpeg")
        ffmpeg_path = "src/javpymed/ffmpeg-bin/mac/ffmpeg"
    else:
        raise OSError(f"Unsupported Operating system : {system}")
    
    if not os.path.isfile(ffmpeg_path):
        raise FileNotFoundError(f"File not found at : {ffmpeg_path}")
    
    if system != "Windows" and not os.access(ffmpeg_path,os.X_OK):
        raise PermissionError(f"{ffmpeg_path} exists but isn't executable. "
            f"Run: chmod +x \"{ffmpeg_path}\"") 
        
    return ffmpeg_path

#method to prevent auto renaming
def unique_output_path(path):
    if not os.path.exists(path):
        return path
    base, ext = os.path.splitext(path)
    counter = 1
    while True:
        candidate = f"{base} ({counter}){ext}"
        if not os.path.exists(candidate):
            return candidate
        counter += 1
        
def build_output_path(input_path, profile):
    desktop = os.path.expanduser("~/Desktop")
    output_folder = os.path.join(desktop, "Converted Files")
    os.makedirs(output_folder, exist_ok=True)
 
    base_name = os.path.splitext(os.path.basename(input_path))[0]
    filename = f"{base_name}_converted.{profile['container']}"
    return unique_output_path(os.path.join(output_folder, filename))

def build_ffmpeg_command(ffmpeg_path, input_path, output_path, profile):
    command = [
        ffmpeg_path,
        "-y",
        "-i", input_path,
        "-vf", f"scale={profile['width']}:{profile['height']}",
        "-r", str(profile["framerate"]),
        "-c:v", profile["video_codec"],
    ]
 
    command += [
        "-c:a", profile["audio_codec"],
        "-b:a", f"{profile['audio_bitrate_kbps']}k",
        "-ac", str(profile["audio_channels"]),
        output_path,
    ]
    return command

DURATION_PATTERN = re.compile(r"Duration:\s*(\d+):(\d+):(\d+\.\d+)")
TIME_PATTERN = re.compile(r"time=(\d+):(\d+):(\d+\.\d+)")
 
 
def to_seconds(hours, minutes, seconds):
    return int(hours) * 3600 + int(minutes) * 60 + float(seconds)

def run_conversion(input_path, profile_name):
    profile = get_profile(profile_name)
    ffmpeg_path = find_ffmpeg()
    output_path = build_output_path(input_path, profile)
    command = build_ffmpeg_command(ffmpeg_path, input_path, output_path, profile)

    process = subprocess.Popen(
        command,
        stderr=subprocess.PIPE,
        stdout=subprocess.DEVNULL,
        universal_newlines=True,
    )
 
    total_seconds = None

    for line in process.stderr:
        if total_seconds is None:
            match = DURATION_PATTERN.search(line)
            if match:
                total_seconds = to_seconds(*match.groups())
 
        match = TIME_PATTERN.search(line)
        if match and total_seconds:
            current_seconds = to_seconds(*match.groups())
            percent = min(100, int((current_seconds / total_seconds) * 100))
            print(f"PROGRESS:{percent}", flush=True)
 
    process.wait()
 
    if process.returncode == 0:
        print(f"DONE:{output_path}", flush=True)
    else:
        print(f"ERROR:ffmpeg exited with code {process.returncode}", flush=True)
        
        
def main():
    parser = argparse.ArgumentParser(description="Convert a video using a device profile.")
    parser.add_argument("--input", required=True, help="Path to the source video")
    parser.add_argument("--profile", required=True, help="Device profile name")
    args = parser.parse_args()
 
    try:
        run_conversion(args.input, args.profile)
    except Exception as ex:
        print(f"ERROR:{ex}", flush=True)
        sys.exit(1)
 
 
if __name__ == "__main__":
    main()