import praw
import sys
import mysql.connector

def read_subreddit_ids_from_database(cursor):
    subreddit_ids = set()

    # Query the subreddit_id column from the subreddits table
    cursor.execute("SELECT subreddit_id FROM subreddits")

    # Fetch all the subreddit IDs
    rows = cursor.fetchall()

    # Add the subreddit IDs to the set
    for row in rows:
        subreddit_id = row[0]
        subreddit_ids.add(subreddit_id)

    return subreddit_ids


reddit = praw.Reddit(client_id='HPd00gvtJR2hHMn0FA4cUg', client_secret='2D8nj6VPSt3iwqPRNMi-2VFfAjjmHg', user_agent='musicrecom')

# Receive username from Java call
username = sys.argv[1] if len(sys.argv) > 1 else None

if username is None:
    print("No username provided.")
    sys.exit(1)


subreddit_check_list = ['private', 'gold_only', 'gold_restricted', 'archived', 'employees_only']
conn = mysql.connector.connect(
    host='localhost',
    port=3306,
    user='root',
    password='root',
    database='redditor'
)

cursor = conn.cursor()

subreddit_ids = read_subreddit_ids_from_database(cursor)

user_subreddits=[]
CommentSubreddits = {}

try:
    redditor = reddit.redditor(username)
except praw.exceptions.NotFound:
    print(f"Redditor '{username}' not found.")
    sys.exit(1)

try:
    comments = redditor.comments.new(limit=20)
    for comment in comments:
        try:
            subReddit = comment.subreddit
            subreddit_type = subReddit.subreddit_type
            if subreddit_type in subreddit_check_list or subReddit.quarantine:
                print(f"Subreddit '{subReddit.display_name}' is suspended, banned, shadowbanned, or private.")
                continue
        except praw.exceptions.Forbidden as e:
            if e.response.status_code == 403:
                print(f"Access to subreddit '{subReddit.display_name}' is forbidden.")
            elif e.response.status_code == 404:
                print(f"The subreddit '{subReddit.display_name}' does not exist.")
            else:
                print(f"An error occurred while accessing the subreddit '{subReddit.display_name}'. Error code: {e.response.status_code}.")
                continue
        except praw.exceptions.ClientException as e:
            if "received a 404" in str(e):
                print(f"The subreddit '{subReddit.display_name}' does not exist.")
            else:
                print(f"An error occurred while accessing the subreddit '{subReddit.display_name}'. Error: {str(e)}")
                continue

        subreddit_id = subReddit.id
        user_subreddits.append(subreddit_id)
        if subreddit_id in subreddit_ids:
            continue
        display_name = subReddit.display_name
        description = subReddit.public_description

        CommentSubreddits[subreddit_id] = (display_name, description)

except Exception as e:
    print(f"An error occurred: {str(e)}")
    sys.exit(1)
try:
    for subreddit_id, (display_name, description) in CommentSubreddits.items():
        query = "INSERT INTO subreddits (subreddit_id, subreddit_name, subreddit_description) VALUES (%s, %s, %s)"
        values = (subreddit_id, display_name, description)
        try:
            cursor.execute(query, values)
        except mysql.connector.Error as err:
            print(f"Error occurred while inserting data for subreddit '{display_name}': {err}")
except Exception as e:
    print(f"An error occurred: {str(e)}")

conn.commit()

for sub_id in user_subreddits:
    print("subreddit id -"+sub_id)
